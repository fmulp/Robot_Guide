//  Copyright (C) 2004-2008, Robotics Equipment Corporation GmbH

#define _USE_MATH_DEFINES
#include <cmath>
#include <iostream>

#include "rec/robotino/com/all.h"
#include "rec/core_lt/utils.h"
#include "rec/core_lt/Timer.h"

using namespace rec::robotino::com;

class MyCom : public Com
{
public:
	MyCom()
	{
	}

	void errorEvent( Error error, const char* errorString )
	{
		std::cerr << "Error: " << errorString << std::endl;
	}

	void connectedEvent()
	{
		std::cout << "Connected." << std::endl;
	}

	void connectionClosedEvent()
	{
		std::cout << "Connection closed." << std::endl;
	}
};

MyCom com;
Motor motor1;
Motor motor2;
Motor motor3;
OmniDrive omniDrive;
Bumper bumper;
Odometry odometry;
double mysqr(double x){
	return x*x;
}

void init( const std::string& hostname)
{
	// Initialize the actors
	motor1.setMotorNumber( 0 );

	motor2.setMotorNumber( 1 );

	motor3.setMotorNumber( 2 );

	// Connect
	std::cout << "Connecting to  "<< hostname << " ... " << std::endl;
	com.setAddress( hostname.c_str() );

	com.connect();

	odometry.set(0, 0, 0);

	std::cout << std::endl << "Connected" << std::endl;
}

void drive(const int x, const int y, const int phi )
{
	const int vel=90;
	rec::core_lt::Timer timer;
	timer.start();

	//while( com.isConnected()
	//	&& false == bumper.value()
	//	&& timer.msecsElapsed() < 10000 )
	double M=sqrt(mysqr(odometry.x()-x)+mysqr(odometry.y()-y));
	while( com.isConnected()
		&& false == bumper.value()
		&& M>30 )
	{
		M=sqrt(mysqr(odometry.x()-x)+mysqr(odometry.y()-y));
		//std::cout<<M<<std::endl;
		omniDrive.setVelocity( vel*((double)(x-odometry.x()))/M, vel*((double)(y-odometry.y()))/M, 0 );
		com.waitForUpdate();
	}
	/*
	while( com.isConnected()
		&& false == bumper.value()
		&& odometry.x() < x )
	{
		omniDrive.setVelocity( 70, 0, 0 );
		com.waitForUpdate();
	}
	while( com.isConnected()
		&& false == bumper.value()
		&& odometry.y() < y )
	{
		omniDrive.setVelocity( 0, 70, 0 );
		com.waitForUpdate();
	}*/
	
	while( com.isConnected()
		&& false == bumper.value()
		&& phi>0
		&& (abs(odometry.phi()-phi)>5) )
	{
		omniDrive.setVelocity( 0, 0, +30 );
		com.waitForUpdate();
	}
	while( com.isConnected()
		&& false == bumper.value()
		&& phi<0
		&& (abs(odometry.phi()-phi)>5) )
	{
		omniDrive.setVelocity( 0, 0, -30 );
		com.waitForUpdate();
	}

}

void destroy()
{
	com.disconnect();
}

int main( int argc, char **argv )
{
	std::string hostname = "172.26.201.128";
	if( argc == 5 )
	{ 
		hostname = argv[1];
		int x = atoi(argv[2]);
		int y = atoi(argv[3]);
		int phi = atoi(argv[4]);

		std::cout << "x=" << x << " y=" << y << " phi= " << phi << std::endl;

		try
		{
			init( hostname );
			drive(x, y, phi );
			destroy();
		}
		catch( const rec::robotino::com::ComException& e )
		{
			std::cerr << "Com Error: " << e.what() << std::endl;
		}
		catch( const std::exception& e )
		{
			std::cerr << "Error: " << e.what() << std::endl;
		}
		catch( ... )
		{
			std::cerr << "Unknow Error" << std::endl;
		}
	}
	else
	{
		std::cerr << "Need arguments" << std::endl;
	}


	std::cout << "Press any key to exit..." << std::endl;
	//rec::core_lt::waitForKey();
}
