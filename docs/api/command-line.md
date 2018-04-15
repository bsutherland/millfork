[< back to index](../index.md)

# Command-line options

## General options

* `--version` – Display the version number and quit.

* `--help` – Displays help the command line option.

* `--` – End of the options, all the following parameters will be treated as input files, even if they look like options.

## I/O options

*  `-o <file>` – Output filename, without extension. Extension will be added automatically, `.prg` for Commodore, `.a2` for Apple and `.xex` for Atari.

* `-s` – Generate also the assembly output. It is not compatible with any assembler, but it serves purely informational purpose. The file has the same nam as the output file and the extension is `.asm`.

* `-g` – Generate also the label file. The label file contains labels with their addresses, with duplicates removed. It can be loaded into the monitor of the Vice emulator for debugging purposes. The file has the same nam as the output file and the extension is `.lbl`.

* `-I  <dir>;<dir>` – The include directories. The current working directory is also an include directory. Those directories are searched for modules and platform definitions.
 
* `-t <platform>` – Target platform. It is loaded from an `.ini` file found in any of the include directories. See also [this document](target-platforms.md).

* `-r <program>` – Run given program after successful compilation. Useful for automatically launching emulators without any external scripting.


## Verbosity options

 * `-q` – Suppress all messages except for errors.

* `-v`, `-vv`, `-vvv` – Increase verbosity, various levels.

## Code generation options

* `-fcmos-ops`, `-fno-cmos-ops` – Whether should emit CMOS opcodes.  
`.ini` equivalent: `emit_cmos`.
Default: yes if targeting a 65C02-compatible architecture, no otherwise.

* `-fillegals`, `-fno-illegals` – Whether should emit illegal (undocumented) NMOS opcodes.  
`.ini` equivalent: `emit_illegals`.
Default: no.

* `-f65ce02-ops`, `-fno-65ce02-ops` – Whether should emit 65CE02 opcodes.  
`.ini` equivalent: `emit_65ce026`. 
Default: yes if targeting 65CE02, no otherwise.

* `-fhuc6280-ops`, `-fno-huc6280-ops` – Whether should emit HuC6280 opcodes.  
`.ini` equivalent: `emit_huc6280`. 
Default: yes if targeting HuC6280, no otherwise.

* `-fno-65816-ops`, `-femulation-65816-ops`, `-fnative-65816-ops` – Which subset of 65816 instructions to support. 
`-fnative-65816-ops` is required to use any 16-bit operations. 
Currently, there is not much support in the compiler for the native mode.
`.ini` equivalent: `emit_65816`. 
Default: native if targeting 65816, no otherwise.

* `-fjmp-fix`, `-fno-jmp-fix` – Whether should prevent indirect JMP bug on page boundary.  
`.ini` equivalent: `prevent_jmp_indirect_bug`.
Default: no if targeting a 65C02-compatible architecture, yes otherwise.

* `-fzp-register`, `-fno-zp-register` – Whether should reserve 2 bytes of zero page as a pseudoregister.
Increases language features. 
`.ini` equivalent: `zeropage_register`.
Default: yes.
                    
* `-fdecimal-mode`, `-fno-decimal-mode` – Whether decimal mode should be available.  
`.ini` equivalent: `decimal_mode`.
Default: no if targeting Ricoh, yes otherwise.

* `-fvariable-overlap`, `-fno-variable-overlap` – Whether variables should overlap if their scopes do not intersect.  
Default: yes.

* `-fbounds-checking`, `-fnobounds-checking` – Whether should insert bounds checking on array access.  
Default: no.

* `-fcompact-dispatch-params`, `-fnocompact-dispatch-params` – 
Whether parameter values in return dispatch statements may overlap other objects. 
This may cause problems if the parameter table is stored next to a hardware register that has side effects when reading.  
`.ini` equivalent: `compact_dispatch_params`. Default: yes.

## Optimization options

* `-O0` – Disable all optimizations.

* `-O`, `-O2`, `-O3` – Optimize code, various levels.

* `-O9` – Optimize code using superoptimizer (experimental). Computationally expensive, decent results.

* `--inline` – Inline functions automatically (experimental). See the [documentation about inlining](../abi/inlining.md). Computationally easy, can give decent gains.

* `--size` – Optimize for size, sacrificing some speed (experimental).

* `--fast` – Optimize for speed, even if it increases the size a bit (experimental).

* `--blast-processing` – Optimize for speed, even if it increases the size a lot (experimental).

* `--dangerous-optimizations` – Use dangerous optimizations (experimental). Dangerous optimizations are more likely to result in broken code.

## Warning options

* `-Wall` – Enable extra warnings.

* `-Wfatal` – Treat warnings as errors.