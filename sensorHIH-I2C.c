#include <stdio.h>
#include <stdlib.h>
#include <linux/i2c-dev.h>
#include <fcntl.h>
#include <string.h>
#include <sys/ioctl.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <unistd.h>

int main(int argc, char **argv)
{
	int fd;                               /* File descriptor*/
	const char *fileName = "/dev/i2c-1";  /* Name of the port we will be using. On Raspberry 2 this is i2c-1, on an older Raspberry Pi 1 this might be i2c-0.*/
	int  address = 0x27;                  /* Address of Honeywell sensor shifted right 1 bit */
	unsigned char buf[4];                 /* Buffer for data read/written on the i2c bus */

	/* Open port (r/w) */
	if ((fd = open(fileName, O_RDWR)) < 0)
	{
		printf("Failed to open i2c port\n");
		exit(1);
	}

	/* Set port options and slave devie address */
	if (ioctl(fd, I2C_SLAVE, address) < 0)
	{
		printf("Unable to get bus access to talk to slave\n");
		exit(1);
	}

	for(;;){
		/* Initiate measurement by sending a zero bit (see datasheet for communication pattern) */
		if ((write(fd,buf,0)) != 0)
		{
			printf("Error writing bit to i2c slave\n");
			exit(1);
		}

		/* Wait for 100ms for measurement to complete.
		 Typical measurement cycle is 36.65ms for each of humidity and temperature, so you may reduce this to 74ms. */
		usleep(100000);

		/* read back data */
		if (read(fd, buf, 4) < 0)
		{
			printf("Unable to read from slave\n");
			exit(1);
		}
		else
		{
			/* Humidity is located in first two bytes */
			int reading_hum = (buf[0] << 8) + buf[1];
			double humidity = reading_hum / 16382.0 * 100.0;
			printf("Humidity: %f\n", humidity);

			/* Temperature is located in next two bytes, padded by two trailing bits */
			int reading_temp = (buf[2] << 6) + (buf[3] >> 2);
			double temperature = reading_temp / 16382.0 * 165.0 - 40;
			printf("Temperature: %f\n", temperature);
		}
	}

	return 0;
}