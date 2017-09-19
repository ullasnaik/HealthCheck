import ConfigParser
sections=['host','ui','soa','o2o', 'odi','documaker','ipm']
print sections

for sec in sections:
   config = ConfigParser.ConfigParser()
   config.read('envdtls.cfg')
   URL = config.get(sec,sec+'_url')
   username = config.get(sec,sec+'_username')
   password = config.get(sec,sec+'_password')
   sname=config.get(sec,sec+'_name')
   try:
    connect(username,password,URL)
    domainConfig()
    serverList=cmo.getServers();
    domainRuntime()
    cd('/ServerLifeCycleRuntimes/')
    print 'STARTCAPTURE'
    for server in serverList:
         name=server.getName()
         cd(name)
         serverState=cmo.getState()
         print  name +':'+serverState
         cd('..')
   except Exception, e: 
    print 'STARTCAPTURE'
    print 'EXCEPTION:UNABLE_TO_CONNECT'
   print  'ENVNAME:'+sname
   print 'ENDCAPTURE'
   disconnect()

