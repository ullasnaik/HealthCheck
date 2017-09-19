import ConfigParser
#config = ConfigParser.ConfigParser()
#config.read('envdtls.cfg')
sections=['documaker', 'host', 'ipm', 'o2o', 'odi', 'soa', 'ui']
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
    allServers=domainRuntimeService.getServerRuntimes();
    print 'STARTCAPTURE'
    if (len(allServers) > 0):
     for tempServer in allServers:
      jdbcServiceRT = tempServer.getJDBCServiceRuntime();
      dataSources = jdbcServiceRT.getJDBCDataSourceRuntimeMBeans();
      if (len(dataSources) > 0):
        for ds in dataSources:
            print ds.getName(),':',ds.getState(),':',ds.getActiveConnectionsAverageCount(),':',ds.getActiveConnectionsCurrentCount(),':',ds.getActiveConnectionsHighCount()
   except Exception, e:
    print 'STARTCAPTURE'
    print 'EXCEPTION:UNABLE_TO_CONNECT'
   print  'ENVNAME:'+sname
   print 'ENDCAPTURE'
   disconnect()

