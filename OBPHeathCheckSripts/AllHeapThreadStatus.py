import ConfigParser
#config = ConfigParser.ConfigParser()
#config.read('envdtls.cfg')
def getHeapData(servernm):   
   try:
           cd("/ServerRuntimes/"+servernm+"/JVMRuntime/"+servernm)
           usedjvm = cmo.getHeapSizeCurrent()
           freejvm = cmo.getHeapFreeCurrent()
           print 'STARTCAPTURE'
           print 'APNDLN:',servernm,':', int(usedjvm) ,':', int(freejvm)
   except Exception, e:
           print 'STARTCAPTURE'
           print servernm,':EXENOHEAPDATA'
           print 'ENDCAPTURE'
   return
   
def getThreadData(servernm):
    try:
           cd('ServerRuntimes/'+servernm+'/ThreadPoolRuntime/ThreadPoolRuntime')
           hoggingThreads = get('HoggingThreadCount')
           totalThreads = float(get('CompletedRequestCount'))
           idleThrds = get('ExecuteThreadIdleCount')
           pending = get('PendingUserRequestCount')
           standby = get('StandbyThreadCount')
           thruput = get('Throughput')
           print 'STARTCAPTURE'
           print  idleThrds,':', pending,':', hoggingThreads,':',standby
    except Exception, e:
           print 'STARTCAPTURE'
           print servernm,':EXENOTHREADDATA'
           print 'ENDCAPTURE'
    return
if __name__== "main":
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
    domainConfig()
    serverNames =cmo.getServers()
    domainRuntime()
    print 'STARTCAPTURE'
    for name in serverNames:
      servernm=name.getName();
      getHeapData(servernm)
      getThreadData(servernm)
   except Exception, e:
    print 'STARTCAPTURE'
    print sec,':EXE_UNABLE_TO_CONNECT'
   print  'ENVNAME:'+sname
   print 'ENDCAPTURE'
   disconnect()

