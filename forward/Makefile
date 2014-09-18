LIBPATH = "%OPENROBOTINOAPI_DIR%\1\lib\win32"
LIBPATH2 = "%OPENROBOTINOAPI_DIR%\share\lib\win32"

INCPATH = "%OPENROBOTINOAPI_DIR%\1\include"
INCPATH2 = "%OPENROBOTINOAPI_DIR%\share\include"

CFLAGS=/c /DUNICODE /D_CONSOLE /DNDEBUG /D_WINDOWS /DWIN32 /I$(INCPATH) /I$(INCPATH2) /EHsc /MD

LFLAGS = /LIBPATH:$(LIBPATH) rec_robotino_com.lib /LIBPATH:$(LIBPATH2) rec_core_lt.lib

CC = cl

TARGET = forward.exe

all: $(TARGET)

clean:
	del /F /Q *.obj $(TARGET) $(TARGET).manifest

.cpp.obj:
  cl $(CFLAGS) $<
  
main.obj: main.cpp

$(TARGET): main.obj
  link $(LFLAGS) /OUT:$(TARGET) main.obj
	copy "%OPENROBOTINOAPI_DIR%\1\bin\win32\robotinocom.dll" .
	copy "%OPENROBOTINOAPI_DIR%\1\bin\win32\rec_robotino_com.dll" .
	copy "%OPENROBOTINOAPI_DIR%\share\bin\win32\rec_core_lt.dll" .
