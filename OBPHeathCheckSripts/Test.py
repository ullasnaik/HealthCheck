import ConfigParser
import traceback
#sections=['host','ui','soa','o2o', 'odi','documaker','ipm']
sections=['host']

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
      domainRuntime()
      cd('AppRuntimeStateRuntime/AppRuntimeStateRuntime')
      AppList = cmo.getApplicationIds()
      print 'STARTCAPTURE'
      for App in AppList:
          currentAppState=cmo.getCurrentState(str(App).split('#', 1)[0],'obphost_cluster1','obphost_server1')
          print  str(App).split('#', 1)[0] ,':', currentAppState
   except Exception,  e:
    traceback.print_exc()
    print 'STARTCAPTURE'
    print 'EXCEPTION:UNABLE_TO_CONNECT'
   print  'ENVNAME:'+sname
   print 'ENDCAPTURE'
   disconnect()


