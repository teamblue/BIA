all:
	make -C /lib/modules/$(shell uname -r)/build M=${PWD} modules

# Used by the kernel module Makefile.
obj-m := snull.o

clean:
	make -C /lib/modules/$(shell uname -r)/build M=${PWD} clean
