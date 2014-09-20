LIBPATH = $(OPENROBOTINOAPI_DIR)/1/lib/linux
LIBPATH2 = $(OPENROBOTINOAPI_DIR)/share/lib/linux
INCPATH = $(OPENROBOTINOAPI_DIR)/1/include
INCPATH2 = $(OPENROBOTINOAPI_DIR)/share/include

CFLAGS = -Dlinux32 -Wall -I$(INCPATH) -I$(INCPATH2)
LDFLAGS += -Wl,-rpath,$(LIBPATH),-rpath,$(LIBPATH2) -L$(LIBPATH) -L$(LIBPATH2) -lrec_robotino_com -lrec_core_lt
CC = g++
OBJ = main.o

TARGET = forward

all: $(TARGET)

clean:
	rm -f *.o $(TARGET)

$(TARGET): $(OBJ)
	$(CC) $? -o $@ $(LDFLAGS)

%.o: %.cpp
	$(CC) $(CFLAGS) -c $? -o $@
