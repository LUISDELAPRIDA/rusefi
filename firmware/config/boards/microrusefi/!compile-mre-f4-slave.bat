
cd ../../..

set PROJECT_BOARD=microrusefi
set PROJECT_CPU=ARCH_STM32F4
set DEFAULT_ENGINE_TYPE = -DDEFAULT_ENGINE_TYPE=BMW_M73_MRE_SLAVE
set EXTRA_PARAMS=-DDUMMY -DEFI_CANBUS_PEDAL=TRUE

call config/boards/common_make.bat


