A simple Java SE app, that measures the local network speed.
It has a simple console interface.
It Uses Java Sockets, 
Uses Java Multicasting (Broadcasting) to find available devices in the network. 
Each device sends Heartbeat (multicast) to group IP address. 

To check the network speed:
Hello, what do you want to do:
1) Check network speed
2) Check open ports on ip
3) Find Ips in local network
4) Find my IP
9) Exit
1
Please choose device to scan
0   192.168.1.5 192.168.1.5
1   192.168.1.3 x-PC
0
Time for transfer 1MB= 2.584s which means 0.3869969MB/s.
Time for transfer 10MB= 30.851 which means 0.03241386MB/s.
MainThread : MainClass : main : Skipped file outZipFile.zip
Time for transfer zipped 0.037s +  0.187s time for zipping
Total Time for unzipped 11MB= 33.435s time for zipping and sending0.224s.

To check open ports on a IP address:
Hello, what do you want to do:
1) Check network speed
2) Check open ports on ip
3) Find Ips in local network
4) Find my IP
9) Exit
2
In method scanIpAddress, please enter address:
192.168.1.5
There is a server on port 22 of 192.168.1.5

Show the reachable IP addresses in the local network:
Hello, what do you want to do:
1) Check network speed
2) Check open ports on ip
3) Find Ips in local network
4) Find my IP
9) Exit
3
In method getMyLocalSubnet
ralink.ralinktech.com is reachable (192.168.1.1)
x-PC is reachable (192.168.1.3)
192.168.1.5 is reachable (192.168.1.5)

Display My IP Adress:
Hello, what do you want to do:
1) Check network speed
2) Check open ports on ip
3) Find Ips in local network
4) Find my IP
9) Exit
4
In method findMyIP
IP: 192.168.1.3
HostName: x-PC
