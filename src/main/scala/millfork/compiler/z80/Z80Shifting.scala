package millfork.compiler.z80

import millfork.CompilationFlag
import millfork.assembly.z80.{NoRegisters, ZLine, ZOpcode}
import millfork.compiler.CompilationContext
import millfork.env.NumericConstant
import millfork.error.ConsoleLogger
import millfork.node._

import scala.collection.GenTraversableOnce

/**
  * @author Karol Stasiak
  */
object Z80Shifting {

  private def calculateIterationCountPlus1(ctx: CompilationContext, rhs: Expression) = {
    Z80ExpressionCompiler.compile8BitTo(ctx, rhs #+# 1, ZRegister.B)
  }

  private def fixAfterShiftIfNeeded(extendedOps: Boolean, left: Boolean, i: Long): List[ZLine] =
    if (extendedOps || left) {
      Nil
    } else {
      List(ZLine.imm8(ZOpcode.AND, 0xff & (0xff >> i)))
    }

  def compile8BitShift(ctx: CompilationContext, lhs: Expression, rhs: Expression, left: Boolean): List[ZLine] = {
    val env = ctx.env
    val extendedOps = ctx.options.flag(CompilationFlag.EmitExtended80Opcodes)
    val op =
      if (extendedOps) {
        if (left) ZLine.register(ZOpcode.ADD, ZRegister.A) else ZLine.register(ZOpcode.SRL, ZRegister.A)
      } else {
        if (left) ZLine.register(ZOpcode.ADD, ZRegister.A) else ZLine.implied(ZOpcode.RRCA)
      }
    val l = Z80ExpressionCompiler.compileToA(ctx, lhs)
    env.eval(rhs) match {
      case Some(NumericConstant(i, _)) =>
        if (i <= 0) {
          l
        } else if (i >= 8) {
          l :+ ZLine.ldImm8(ZRegister.A, 0)
        } else {
          l ++ List.tabulate(i.toInt)(_ => op) ++ fixAfterShiftIfNeeded(extendedOps, left, i)
        }
      case _ =>
        val calcCount = calculateIterationCountPlus1(ctx, rhs)
        val l = Z80ExpressionCompiler.stashBCIfChanged(ctx, Z80ExpressionCompiler.compileToA(ctx, lhs))
        val loopBody = op :: fixAfterShiftIfNeeded(extendedOps, left, 1)
        val labelL = ctx.nextLabel("sh")
        val labelS = ctx.nextLabel("sh")
        calcCount ++ l ++ List(ZLine.jumpR(ctx, labelS), ZLine.label(labelL)) ++ loopBody ++ List(ZLine.label(labelS)) ++ ZLine.djnz(ctx, labelL)
    }
  }

  def compile8BitShiftInPlace(ctx: CompilationContext, lhs: LhsExpression, rhs: Expression, left: Boolean): List[ZLine] = {
    val env = ctx.env
    val extendedOps = ctx.options.flag(CompilationFlag.EmitExtended80Opcodes)
    val (op, opLine) =
      if (extendedOps) {
        if (left) ZOpcode.ADD -> ZLine.register(ZOpcode.ADD, ZRegister.A) else ZOpcode.SRL -> ZLine.register(ZOpcode.SRL, ZRegister.A)
      } else {
        if (left) ZOpcode.ADD -> ZLine.register(ZOpcode.ADD, ZRegister.A) else ZOpcode.LABEL -> ZLine.implied(ZOpcode.RRCA)
      }
    env.eval(rhs) match {
      case Some(NumericConstant(i, _)) =>
        if (i <= 0) {
          Z80ExpressionCompiler.compileToA(ctx, lhs)
        } else if (i >= 8) {
          ZLine.ldImm8(ZRegister.A, 0) :: Z80ExpressionCompiler.storeA(ctx, lhs, signedSource = false)
        } else {
          Z80ExpressionCompiler.calculateAddressToAppropriatePointer(ctx, lhs, forWriting = true) match {
            case Some((register, l)) =>
              // for shifting left:
              // ADD A = 4 cycles
              // SRL (HL) = 15 cycles
              // LD A,(HL) or LD (HL),A = 7 cycles
              // SRL (IX+d) = 23 cycles
              // LD A,(IX+d) or LD (IX+d),A = 19 cycles
              // when using A is profitable or equal?
              // for HL:
              // 15x >= 7 + 4x + 7
              // 11x >= 14
              // x >= 14/11
              // for IX+d
              // 23x >= 19 + 4x + 19
              // 19x >= 38
              // x >= 2
              // so:
              // - for x == 1, don't use A
              // - for x >= 2, use A
              //
              // for shifting right:
              // SRL A = 8 cycles
              // SRL (HL) = 15 cycles
              // LD A,(HL) or LD (HL),A = 7 cycles
              // SRL (IX+d) = 23 cycles
              // LD A,(IX+d) or LD (IX+d),A = 19 cycles
              // when using A is profitable?
              // for HL:
              // 15x > 7 + 8x + 7
              // 7x > 14
              // x > 2
              // for IX+d
              // 23x > 19 + 8x + 19
              // 15x > 38
              // x > 2.5
              // so:
              // - for x <= 2, don't use A
              // - for x >= 3, use A
              if (extendedOps && i <= 2 && op != ZOpcode.ADD) {
                l ++ List.tabulate(i.toInt)(_ => ZLine.register(op, register))
              } else if (extendedOps && i == 1 && op == ZOpcode.ADD) {
                l ++ List.tabulate(i.toInt)(_ => ZLine.register(ZOpcode.SLA, register))
              } else {
                l ++ List(ZLine.ld8(ZRegister.A, register)) ++
                  List.tabulate(i.toInt)(_ => opLine) ++
                  fixAfterShiftIfNeeded(extendedOps, left, i) ++
                  List(ZLine.ld8(register, ZRegister.A))
              }
          }
        }
      case _ =>
        val calcCount = calculateIterationCountPlus1(ctx, rhs)
        val l = Z80ExpressionCompiler.stashBCIfChanged(ctx, Z80ExpressionCompiler.compileToA(ctx, lhs))
        val loopBody = ZLine.register(op, ZRegister.A) :: fixAfterShiftIfNeeded(extendedOps, left, 1)
        val labelL = ctx.nextLabel("sh")
        val labelS = ctx.nextLabel("sh")
        calcCount ++ l ++ List(ZLine.jumpR(ctx, labelS), ZLine.label(labelL)) ++ loopBody ++ List(ZLine.label(labelS)) ++ ZLine.djnz(ctx, labelL) ++ Z80ExpressionCompiler.storeA(ctx, lhs, signedSource = false)
    }
  }

  def compile16BitShift(ctx: CompilationContext, lhs: Expression, rhs: Expression, left: Boolean): List[ZLine] = {
    val env = ctx.env
    val extendedOps = ctx.options.flag(CompilationFlag.EmitExtended80Opcodes)
    val l = Z80ExpressionCompiler.compileToHL(ctx, lhs)
    env.eval(rhs) match {
      case Some(NumericConstant(i, _)) =>
        if (i <= 0) {
          l
        } else if (i >= 16) {
          l :+ ZLine.ldImm16(ZRegister.HL, 0)
//        } else if (i > 8) { // TODO: optimize shifts larger than 8
//          ???
        } else {
          if (left) {
            l ++ List.tabulate(i.toInt)(_ => ZLine.registers(ZOpcode.ADD_16, ZRegister.HL, ZRegister.HL))
          } else {
            if (extendedOps) {
              l ++ (0L until i).flatMap(_ => List(
                ZLine.register(ZOpcode.SRL, ZRegister.H),
                ZLine.register(ZOpcode.RR, ZRegister.L)
              ))
            } else {
              l ++ (1L until i).flatMap(_ => List(
                ZLine.ld8(ZRegister.A, ZRegister.H),
                ZLine.register(ZOpcode.OR, ZRegister.A),
                ZLine.implied(ZOpcode.RRA),
                ZLine.ld8(ZRegister.H, ZRegister.A),
                ZLine.ld8(ZRegister.A, ZRegister.L),
                ZLine.implied(ZOpcode.RRA),
                ZLine.ld8(ZRegister.L, ZRegister.A)
              )) ++ List(
                ZLine.ld8(ZRegister.A, ZRegister.H),
                ZLine.register(ZOpcode.OR, ZRegister.A),
                ZLine.implied(ZOpcode.RRA),
                ZLine.ld8(ZRegister.H, ZRegister.A),
                ZLine.ld8(ZRegister.A, ZRegister.L),
                ZLine.implied(ZOpcode.RRA),
                ZLine.ld8(ZRegister.L, ZRegister.A)
              )
            }
          }
        }
      case _ =>
        val calcCount = calculateIterationCountPlus1(ctx, rhs)
        val loopBody =
          if (extendedOps) {
            if (left) {
              List(
                ZLine.register(ZOpcode.SLA, ZRegister.L),
                ZLine.register(ZOpcode.RL, ZRegister.H))
            } else {
              List(
                ZLine.register(ZOpcode.SRL, ZRegister.H),
                ZLine.register(ZOpcode.RR, ZRegister.L))
            }
          } else {
            if (left) {
              List(
                ZLine.ld8(ZRegister.A, ZRegister.L),
                ZLine.register(ZOpcode.ADD, ZRegister.A),
                ZLine.ld8(ZRegister.L, ZRegister.A),
                ZLine.ld8(ZRegister.A, ZRegister.H),
                ZLine.implied(ZOpcode.RLA),
                ZLine.ld8(ZRegister.H, ZRegister.A))
            } else {
              List(
                ZLine.ld8(ZRegister.A, ZRegister.H),
                ZLine.register(ZOpcode.OR, ZRegister.A),
                ZLine.implied(ZOpcode.RRA),
                ZLine.ld8(ZRegister.H, ZRegister.A),
                ZLine.ld8(ZRegister.A, ZRegister.L),
                ZLine.implied(ZOpcode.RRA),
                ZLine.ld8(ZRegister.L, ZRegister.A))
            }
          }
        val labelS = ctx.nextLabel("sh")
        val labelL = ctx.nextLabel("sh")
        calcCount ++ l ++ List(ZLine.jumpR(ctx, labelS), ZLine.label(labelL)) ++ loopBody ++ List(ZLine.label(labelS)) ++ ZLine.djnz(ctx, labelL)
    }
  }

  def compileNonetShiftRight(ctx: CompilationContext, rhs: Expression): List[ZLine] = {
    import ZOpcode._
    import ZRegister._
    val extended = ctx.options.flag(CompilationFlag.EmitExtended80Opcodes)
    ctx.env.eval(rhs) match {
      case Some(NumericConstant(0, _)) =>
        List(ZLine.ld8(A, L))
      case Some(NumericConstant(n, _)) if n < 0 =>
        ctx.log.error("Negative shift amount", rhs.position) // TODO
        Nil
      case Some(NumericConstant(n, _)) if n >= 9 =>
        List(ZLine.ldImm8(A, 0))
      case Some(NumericConstant(1, _)) =>
        if (extended)
          List(ZLine.register(SRL, H), ZLine.ld8(A, L), ZLine.register(RR, A))
        else
          List(ZLine.ld8(A, H), ZLine.implied(RRA), ZLine.ld8(A, L), ZLine.implied(RRA))
      case Some(NumericConstant(2, _)) if extended=>
          List(ZLine.register(SRL, H), ZLine.ld8(A, L), ZLine.implied(RRA), ZLine.register(SRL, A))
      case Some(NumericConstant(n, _)) =>
        if (extended)
          List(ZLine.register(SRL, H), ZLine.ld8(A, L)) ++ (List.fill(n.toInt)(ZLine.implied(RRA)) :+ ZLine.imm8(AND, 0x1ff >> n))
        else
          List(ZLine.ld8(A, H), ZLine.implied(RRA), ZLine.ld8(A, L)) ++ (List.fill(n.toInt)(ZLine.implied(RRA)) :+ ZLine.imm8(AND, 0x1ff >> n))

      case _ =>
        ctx.log.error("Non-constant shift amount", rhs.position) // TODO
        Nil
    }
  }

  def compileLongShiftInPlace(ctx: CompilationContext, lhs: LhsExpression, rhs: Expression, size: Int, left: Boolean): List[ZLine] = {
    val extended = ctx.options.flag(CompilationFlag.EmitExtended80Opcodes)
    val store = Z80ExpressionCompiler.compileByteStores(ctx, lhs, size, includeStep = false)
    val loadLeft = Z80ExpressionCompiler.compileByteReads(ctx, lhs, size, ZExpressionTarget.HL)
    val shiftOne = if (left) {
      loadLeft.zip(store).zipWithIndex.flatMap {
        case ((ld, st), ix) =>
          import ZOpcode._
          import ZRegister._
          val shiftByte =
            if (ix == 0) List(ZLine.register(ADD, A))
            else List(ZLine.implied(RLA))
          ld ++ shiftByte ++ st
      }
    } else {
      loadLeft.reverse.zip(store.reverse).zipWithIndex.flatMap {
        case ((ld, st), ix) =>
          import ZOpcode._
          import ZRegister._
          val shiftByte = if (ix == 0) {
            if (extended) List(ZLine.register(SRL, A))
            else List(ZLine.register(OR, A), ZLine.implied(RRA))
          } else List(ZLine.implied(RRA))
          ld ++ shiftByte ++ st
      }
    }
    ctx.env.eval(rhs) match {
      case Some(NumericConstant(0, _)) => Nil
      case Some(NumericConstant(n, _)) if n < 0 =>
        ctx.log.error("Negative shift amount", rhs.position) // TODO
        Nil
      case Some(NumericConstant(n, _)) =>
        List.fill(n.toInt)(shiftOne).flatten
      case _ =>
        val calcCount = calculateIterationCountPlus1(ctx, rhs)
        val labelL = ctx.nextLabel("sh")
        val labelS = ctx.nextLabel("sh")
        calcCount ++ List(ZLine.jumpR(ctx, labelS), ZLine.label(labelL)) ++ shiftOne ++ List(ZLine.label(labelS)) ++ ZLine.djnz(ctx, labelL)
    }
  }

}
