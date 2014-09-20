//  Copyright (C) 2004-2008, Robotics Equipment Corporation GmbH

#define _USE_MATH_DEFINES
#include <cmath>
#include <iostream>
#include <ctime>

#include "rec/robotino/com/all.h"
#include "rec/core_lt/utils.h"
#include "rec/core_lt/Timer.h"

using namespace rec::robotino::com;
using namespace std;
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
DistanceSensor sharps[9];
double mysqr(double x){
	return x*x;
}
float getDist(float V){
	return ( ( 17/(V+0.1) )-2 );
}
void init( const std::string& hostname)
{
	time_t now = time(0);
   
   // convert now to string form
   char* dt = ctime(&now);

   cout << "The local date and time is: " << dt<< endl;
	// Initialize the actors
	motor1.setMotorNumber( 0 );

	motor2.setMotorNumber( 1 );

	motor3.setMotorNumber( 2 );

	// Connect
	std::cout << "Connecting to  "<< hostname << " ... " << std::endl;
	com.setAddress( hostname.c_str() );

	com.connect();
	
	odometry.set(0, 0, 0);
	
	for(int c=0;c<9;c++){
		sharps[c].setSensorNumber(c);
	}
	std::cout << std::endl << "Connected" << std::endl;
}

void drive(int x, int y, const int phi )
{
	const int vel=90;
	rec::core_lt::Timer timer;
	timer.start();
	double phi1=57.29577951*acos((x-odometry.x())/(sqrt(mysqr(odometry.x()-x)+mysqr(odometry.y()-y)) ));
	cout<<phi1<<endl;
	
	while( com.isConnected()
		&& false == bumper.value()
		&& y>0
		&& (abs(odometry.phi()-phi1)>1) )
	{
		cout<<odometry.phi()<<endl;
		omniDrive.setVelocity( 0, 0, +30 );
		com.waitForUpdate();
	}
	while( com.isConnected()
		&& false == bumper.value()
		&& y<0
		&& (abs(odometry.phi()+phi1)>1) )
	{
		cout<<odometry.phi()<<endl;
		omniDrive.setVelocity( 0, 0, -30 );
		com.waitForUpdate();
	}
	x=(sqrt(mysqr(odometry.x()-x)+mysqr(odometry.y()-y)) );
	y=0;
	odometry.set(0, 0, 0);
	
	bool flag=false;
	//cout<<"<P>move"<<endl;
	double M=sqrt(mysqr(odometry.x()-x)+mysqr(odometry.y()-y));
	while( com.isConnected()
		&& false == bumper.value()
		&& M>30 )
	{
		flag=true;
		while( (getDist(sharps[8].voltage())<30)
			||(getDist(sharps[0].voltage())<30)
			||(getDist(sharps[1].voltage())<30) ){
			omniDrive.setVelocity(0,0,0);
			//if(flag);cout<<"<P>stop"<<endl;
			flag=false;
		}
		//if(!flag)cout<<"<P>move"<<endl;
		M=sqrt(mysqr(odometry.x()-x)+mysqr(odometry.y()-y));
		omniDrive.setVelocity( vel*((double)(x-odometry.x()))/M, vel*((double)(y-odometry.y()))/M, 0 );
		com.waitForUpdate();
	}
	
	odometry.set(0, 0, 0);
	
	int v_phi=30*(phi/abs(phi));
	cout<<"!"<<(abs(abs(odometry.phi())-abs(phi)))<<endl;
	while( com.isConnected()
		&& false == bumper.value()
		&& (abs(abs(odometry.phi())-abs(phi))>1))
	{
		cout<<"!"<<(abs(abs(odometry.phi())-abs(phi)))<<endl;
		omniDrive.setVelocity( 0, 0, v_phi );
		com.waitForUpdate();
	}
	

	/*while( com.isConnected()
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
	}*/

}

void destroy()
{
	com.disconnect();
}

int main( int argc, char **argv )
{
	std::string hostname = "172.26.201.128";
	char *data=NULL;
	long x1=-1,y1=-1,phi1=-1;
	printf("%s%c%c\n",
	"Content-Type:text/html;charset=iso-8859-1",13,10);
	printf("<TITLE>Multiplication results</TITLE>\n");
	printf("<H3>Multiplication results</H3>\n");
	data = getenv("QUERY_STRING");
	if(data == NULL)
	  printf("<P>Error! Error in passing data from form to script.");
	else if(sscanf(data,"%ld&%ld&%ld",&x1,&y1,&phi1)!=3)
	  printf("<P>Error! Invalid data. Data must be numeric.");
	else{
		  
	}
	printf("<P>x=%ld y=%ld phi=%ld.",x1,y1,phi1);
	if(data!=NULL)printf("<P>%s",data);
		  	  
	
	if( true )//if( argc == 5 )
	{ 
		//hostname = argv[1];
		//int x = atoi(argv[2]);
		//int y = atoi(argv[3]);
		//int phi = atoi(argv[4]);
		int x = x1;
		int y = y1;
		int phi = phi1;

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

	
