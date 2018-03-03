package millfork

import millfork.assembly.opt._
import millfork.node.opt.{UnreachableCode, UnusedFunctions, UnusedGlobalVariables, UnusedLocalVariables}

/**
  * @author Karol Stasiak
  */
object OptimizationPresets {
  val NodeOpt = List(
    UnreachableCode,
    UnusedFunctions,
    UnusedLocalVariables,
    UnusedGlobalVariables,
  )
  val AssOpt: List[AssemblyOptimization] = List[AssemblyOptimization](
    AlwaysGoodOptimizations.NonetAddition,
    AlwaysGoodOptimizations.PointlessSignCheck,
    AlwaysGoodOptimizations.PoinlessLoadBeforeAnotherLoad,
    AlwaysGoodOptimizations.PointlessLoadAfterLoadOrStore,
    LaterOptimizations.PointessLoadingForShifting,
    AlwaysGoodOptimizations.SimplifiableBitOpsSequence,
    AlwaysGoodOptimizations.IdempotentDuplicateRemoval,
    AlwaysGoodOptimizations.BranchInPlaceRemoval,
    UnusedLabelRemoval,
    AlwaysGoodOptimizations.UnconditionalJumpRemoval,
    UnusedLabelRemoval,
    AlwaysGoodOptimizations.RearrangeMath,
    LaterOptimizations.PointlessLoadAfterStore,
    AlwaysGoodOptimizations.PoinlessLoadBeforeAnotherLoad,
    AlwaysGoodOptimizations.PointlessOperationAfterLoad,
    AlwaysGoodOptimizations.PointlessLoadBeforeTransfer,
    VariableToRegisterOptimization,
    AlwaysGoodOptimizations.PoinlessLoadBeforeAnotherLoad,
    AlwaysGoodOptimizations.CommonIndexSubexpressionElimination,
    AlwaysGoodOptimizations.PointlessOperationPairRemoval,
    AlwaysGoodOptimizations.PointlessOperationPairRemoval2,
    AlwaysGoodOptimizations.PoinlessLoadBeforeAnotherLoad,
    LaterOptimizations.PointlessLoadAfterStore,
    AlwaysGoodOptimizations.PointlessOperationAfterLoad,
    AlwaysGoodOptimizations.IdempotentDuplicateRemoval,
    LoopUnrolling.LoopUnrolling,
    AlwaysGoodOptimizations.ConstantIndexPropagation,
    AlwaysGoodOptimizations.PointlessLoadBeforeReturn,
    AlwaysGoodOptimizations.PoinlessFlagChange,
    AlwaysGoodOptimizations.FlagFlowAnalysis,
    AlwaysGoodOptimizations.ConstantFlowAnalysis,
    AlwaysGoodOptimizations.PointlessMath,
    AlwaysGoodOptimizations.PointlessOperationFromFlow,
    VariableToRegisterOptimization,
    ChangeIndexRegisterOptimizationPreferringX2Y,
    VariableToRegisterOptimization,
    ChangeIndexRegisterOptimizationPreferringY2X,
    VariableToRegisterOptimization,
    AlwaysGoodOptimizations.ConstantFlowAnalysis,
    LaterOptimizations.DoubleLoadToDifferentRegisters,
    LaterOptimizations.DoubleLoadToTheSameRegister,
    LaterOptimizations.DoubleLoadToDifferentRegisters,
    LaterOptimizations.DoubleLoadToTheSameRegister,
    LaterOptimizations.DoubleLoadToDifferentRegisters,
    LaterOptimizations.DoubleLoadToTheSameRegister,
    LaterOptimizations.DoubleLoadToTwoRegistersWhenOneWillBeTrashed,
    EmptyMemoryStoreRemoval,
    AlwaysGoodOptimizations.PointlessOperationFromFlow,
    AlwaysGoodOptimizations.PoinlessLoadBeforeAnotherLoad,
    AlwaysGoodOptimizations.IdempotentDuplicateRemoval,
    AlwaysGoodOptimizations.ConstantIndexPropagation,
    AlwaysGoodOptimizations.ConstantFlowAnalysis,
    AlwaysGoodOptimizations.PointlessRegisterTransfers,
    AlwaysGoodOptimizations.PointlessRegisterTransfersBeforeCompare,
    AlwaysGoodOptimizations.PointlessRegisterTransfersBeforeReturn,
    AlwaysGoodOptimizations.PointlessRegisterTransfersBeforeStore,
    AlwaysGoodOptimizations.PointlessStashingToIndexOverShortSafeBranch,
    AlwaysGoodOptimizations.PointlessStackStashing,
    AlwaysGoodOptimizations.RearrangeMath,
    EmptyMemoryStoreRemoval,
    AlwaysGoodOptimizations.PointlessLoadBeforeReturn,
    LaterOptimizations.PointessLoadingForShifting,
    AlwaysGoodOptimizations.SimplifiableBitOpsSequence,
    AlwaysGoodOptimizations.SimplifiableBitOpsSequence,
    AlwaysGoodOptimizations.SimplifiableBitOpsSequence,
    AlwaysGoodOptimizations.SimplifiableBitOpsSequence,

    LaterOptimizations.LoadingAfterShifting,
    EmptyMemoryStoreRemoval,
    AlwaysGoodOptimizations.PoinlessStoreBeforeStore,
    LaterOptimizations.PointlessLoadAfterStore,
    AlwaysGoodOptimizations.PoinlessLoadBeforeAnotherLoad,
    AlwaysGoodOptimizations.RearrangableLoadFromTheSameLocation,

    LaterOptimizations.LoadingAfterShifting,
    EmptyMemoryStoreRemoval,
    AlwaysGoodOptimizations.PoinlessStoreBeforeStore,
    LaterOptimizations.PointlessLoadAfterStore,
    AlwaysGoodOptimizations.PoinlessLoadBeforeAnotherLoad,

    LaterOptimizations.LoadingAfterShifting,
    EmptyMemoryStoreRemoval,
    AlwaysGoodOptimizations.PoinlessStoreBeforeStore,
    LaterOptimizations.PointlessLoadAfterStore,
    AlwaysGoodOptimizations.PoinlessLoadBeforeAnotherLoad,
    AlwaysGoodOptimizations.TailCallOptimization,
    AlwaysGoodOptimizations.UnusedCodeRemoval,
    AlwaysGoodOptimizations.ReverseFlowAnalysis,
    AlwaysGoodOptimizations.ModificationOfJustWrittenValue,
    AlwaysGoodOptimizations.PointlessMathFromFlow,
    AlwaysGoodOptimizations.PointlessMathFromFlow,
    AlwaysGoodOptimizations.PointlessMathFromFlow,
    AlwaysGoodOptimizations.PointlessMathFromFlow,
    AlwaysGoodOptimizations.PointlessMathFromFlow,
    AlwaysGoodOptimizations.PointlessMathFromFlow,
    AlwaysGoodOptimizations.PointlessMathFromFlow,
    AlwaysGoodOptimizations.PointlessMathFromFlow,
    AlwaysGoodOptimizations.SimplifiableCondition,
    AlwaysGoodOptimizations.IncrementingIndexRegistersAfterTransfer,
    AlwaysGoodOptimizations.MathOperationOnTwoIdenticalMemoryOperands,
    LaterOptimizations.UseZeropageAddressingMode,

    LaterOptimizations.UseXInsteadOfStack,
    LaterOptimizations.UseYInsteadOfStack,
    LaterOptimizations.IndexSwitchingOptimization,
    LaterOptimizations.LoadingBranchesOptimization,
    LaterOptimizations.IncreaseWithLimit,
    SingleAssignmentVariableOptimization,
  )

  val Good: List[AssemblyOptimization] = List[AssemblyOptimization](
    AlwaysGoodOptimizations.Adc0Optimization,
    AlwaysGoodOptimizations.BitPackingUnpacking,
    AlwaysGoodOptimizations.BranchInPlaceRemoval,
    AlwaysGoodOptimizations.CarryFlagConversion,
    DangerousOptimizations.ConstantIndexOffsetPropagation,
    AlwaysGoodOptimizations.CommonBranchBodyOptimization,
    AlwaysGoodOptimizations.CommonExpressionInConditional,
    AlwaysGoodOptimizations.CommonIndexSubexpressionElimination,
    AlwaysGoodOptimizations.ConstantPointer,
    AlwaysGoodOptimizations.ConstantFlowAnalysis,
    AlwaysGoodOptimizations.ConstantIndexPropagation,
    AlwaysGoodOptimizations.DoubleJumpSimplification,
    EmptyMemoryStoreRemoval,
    AlwaysGoodOptimizations.FlagFlowAnalysis,
    AlwaysGoodOptimizations.IdempotentDuplicateRemoval,
    AlwaysGoodOptimizations.ImpossibleBranchRemoval,
    AlwaysGoodOptimizations.IncrementingIndexRegistersAfterTransfer,
    AlwaysGoodOptimizations.IndexComparisonOptimization,
    AlwaysGoodOptimizations.IndexSequenceOptimization,
    LoopUnrolling.LoopUnrolling,
    AlwaysGoodOptimizations.MathOperationOnTwoIdenticalMemoryOperands,
    AlwaysGoodOptimizations.ModificationOfJustWrittenValue,
    AlwaysGoodOptimizations.NonetAddition,
    AlwaysGoodOptimizations.OperationsAroundShifting,
    AlwaysGoodOptimizations.PoinlessFlagChange,
    AlwaysGoodOptimizations.PointlessLoadAfterLoadOrStore,
    AlwaysGoodOptimizations.PoinlessLoadBeforeAnotherLoad,
    AlwaysGoodOptimizations.PointlessLoadBeforeReturn,
    AlwaysGoodOptimizations.PointlessLoadBeforeTransfer,
    AlwaysGoodOptimizations.PointlessMath,
    AlwaysGoodOptimizations.PointlessMathFromFlow,
    AlwaysGoodOptimizations.PointlessOperationAfterLoad,
    AlwaysGoodOptimizations.PointlessOperationFromFlow,
    AlwaysGoodOptimizations.PointlessOperationPairRemoval,
    AlwaysGoodOptimizations.PointlessOperationPairRemoval2,
    AlwaysGoodOptimizations.PointlessRegisterTransfers,
    AlwaysGoodOptimizations.PointlessRegisterTransfersBeforeCompare,
    AlwaysGoodOptimizations.PointlessRegisterTransfersBeforeReturn,
    AlwaysGoodOptimizations.PointlessSignCheck,
    AlwaysGoodOptimizations.PointlessStackStashing,
    AlwaysGoodOptimizations.PointlessStashingToIndexOverShortSafeBranch,
    AlwaysGoodOptimizations.PoinlessStoreBeforeStore,
    AlwaysGoodOptimizations.RearrangableLoadFromTheSameLocation,
    AlwaysGoodOptimizations.RearrangeMath,
    AlwaysGoodOptimizations.RemoveNops,
    AlwaysGoodOptimizations.ReverseFlowAnalysis,
    AlwaysGoodOptimizations.SimplifiableBitOpsSequence,
    AlwaysGoodOptimizations.SimplifiableCondition,
    AlwaysGoodOptimizations.SimplifiableStackOperation,
    AlwaysGoodOptimizations.SmarterShiftingOfWords,
    AlwaysGoodOptimizations.SmarterShiftingBytes,
    AlwaysGoodOptimizations.UnconditionalJumpRemoval,
    UnusedLabelRemoval,
    AlwaysGoodOptimizations.TailCallOptimization,
    AlwaysGoodOptimizations.UnusedCodeRemoval,
    AlwaysGoodOptimizations.UnusedLabelRemoval,
    VariableToRegisterOptimization,
  )

  val QuickPreset: List[AssemblyOptimization] = List[AssemblyOptimization](
    AlwaysGoodOptimizations.Adc0Optimization,
    AlwaysGoodOptimizations.BranchInPlaceRemoval,
    AlwaysGoodOptimizations.CommonBranchBodyOptimization,
    AlwaysGoodOptimizations.CommonExpressionInConditional,
    AlwaysGoodOptimizations.CommonIndexSubexpressionElimination,
    AlwaysGoodOptimizations.IndexSequenceOptimization,
    AlwaysGoodOptimizations.PoinlessStoreBeforeStore,
    AlwaysGoodOptimizations.PointlessLoadAfterLoadOrStore,
    AlwaysGoodOptimizations.PointlessLoadBeforeTransfer,
    AlwaysGoodOptimizations.PointlessOperationFromFlow,
    AlwaysGoodOptimizations.ReverseFlowAnalysis,
    AlwaysGoodOptimizations.SimplifiableCondition,
    VariableToRegisterOptimization,
    LaterOptimizations.DoubleLoadToTheSameRegister
  )
}
