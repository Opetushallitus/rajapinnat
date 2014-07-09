/*
 * Copyright (c) 2012 The Finnish Board of Education - Opetushallitus
 *
 * This program is free software:  Licensed under the EUPL, Version 1.1 or - as
 * soon as they will be approved by the European Commission - subsequent versions
 * of the EUPL (the "Licence");
 *
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at: http://www.osor.eu/eupl/
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * European Union Public Licence for more details.
 */
package fi.vm.sade.rajapinnat.kela;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

/**
 * 
 * @author Markus
 */
@Component
@Configurable
public class KelaGenerator implements Runnable {
	
    private static final Logger LOG = Logger.getLogger(KelaGenerator.class);

    Map<String,AbstractOPTIWriter> allOptiWriters = new HashMap<String,AbstractOPTIWriter>();
    @Autowired
    private WriteOPTILI optiliWriter;
    @Autowired
    private WriteOPTINI optiniWriter;
    @Autowired
    private WriteOPTIOL optiolWriter;
    @Autowired
    private WriteOPTITU optituWriter;
    @Autowired
    private WriteOPTIYH optiyhWriter;
    @Autowired
    private WriteOPTIYT optiytWriter;
    @Autowired
    private WriteOPTIOR optiorWriter;
    @Autowired
    private WriteLOG optiLog;  // NOTE: the only purpose of this class is to neatly get LOG file name 

	private List<AbstractOPTIWriter> selectedOptiWriters = new LinkedList<AbstractOPTIWriter>();

    @Autowired
    private OrganisaatioContainer orgContainer;
    
    @Autowired
    ProducerTemplate producerTemplate;
    
    long startTime = 0;
    long endTime = 0;
    
    private String protocol;

    private String host;
    private String username;
    private String password;
    private String sourcePath;
    private String targetPath;
    private String dataTimeout;

	private boolean sendonly = false;
	private boolean generateonly = false;

	public void setSendonly(boolean sendonly) {
		this.sendonly = sendonly;
	}

	public void setGenerateonly(boolean generateonly) {
		this.generateonly = generateonly;
	}

	/**
     * Generates all KELA-OPTI transfer files currently implemented
	 * @throws UserStopRequestException 
     */
    public void generateKelaFiles() throws UserStopRequestException {
        LOG.info("Fetching organisaatiot from index...");
        long time = System.currentTimeMillis();
        long startTime = time;
        orgContainer.fetchOrganisaatiot();
        LOG.info("Fetch time: " + (System.currentTimeMillis() - time)/1000.0 + " seconds");
        for (AbstractOPTIWriter optiWriter : selectedOptiWriters) {
        	writeKelaFile(optiWriter);
        }
        LOG.info("All files generated");
        LOG.info("Generation time: " + (System.currentTimeMillis() - startTime)/1000.0 + " seconds");
    }
    
    /**
     * Performs ftp transfer of generated kela-opti files.
     * @throws Exception
     */
    
	private String mkTargetUrl(String protocol, String username,
	String host, String targetPath, String password,
	String dataTimeout) {
		return String.format("%s%s%s%s%s%s%s%s%s%s", 
		                protocol, 
		                "://", 
		                username, 
		                "@", 
		                host, 
		                targetPath, 
		                "?password=", 
		                password,
		                "&ftpClient.dataTimeout=",
		                dataTimeout + "&passiveMode=true");
	}

    private String targetUrl;
    public void transferFiles() throws Exception {
        LOG.info("transferFiles: target url: " + mkTargetUrl(protocol, username, host, targetPath, "???", dataTimeout));
        targetUrl = mkTargetUrl(protocol, username, host, targetPath, password, dataTimeout);
        for (AbstractOPTIWriter optiWriter : selectedOptiWriters) {
        	sendFile(optiWriter);
        }
        LOG.info("Files transferred");
    }
    
    private void sendFile(AbstractOPTIWriter writer) {
    	LOG.info("Sending: " + writer.getFileName()+"...");
    	producerTemplate.sendBodyAndHeader(targetUrl, new File(writer.getFileName()), Exchange.FILE_NAME, writer.getFileLocalName());
    	LOG.info("Done.");
    }
    
    private AbstractOPTIWriter inTurn;
    private void writeKelaFile(AbstractOPTIWriter kelaWriter) throws UserStopRequestException {
        try {
        	inTurn = kelaWriter;
            long time = System.currentTimeMillis();
            LOG.info(String.format("Generating file %s...",kelaWriter.getFileName()));
            kelaWriter.writeStream();
            LOG.info("Generation time: " + (System.currentTimeMillis() - time)/1000.0 + " seconds");
        } catch (UserStopRequestException e) {
        	throw e;
        } catch (Exception ex) {
        	LOG.error(ex.getMessage());
            ex.printStackTrace();
        } 
    }
    
    @Value("${transferprotocol}")
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    @Value("${transferhost}")
    public void setHost(String host) {
        this.host = host;
    }

    @Value("${transferuser}")
    public void setUsername(String username) {
        this.username = username;
    }

    @Value("${transferpassword}")
    public void setPassword(String password) {
        this.password = password;
    }

    @Value("${exportdir}")
    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }

    @Value("${targetPath}")
    public void setTargetPath(String targetPath) {
        this.targetPath = targetPath;
    }

    @Value("${dataTimeout}")
    public void setDataTimeout(String dataTimeout) {
        this.dataTimeout = dataTimeout;
    }
    
    public String getDataTimeout() {
        return dataTimeout;
    }
    
    public String getProtocol() {
        return protocol;
    }

    public String getHost() {
        return host;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getSourcePath() {
        return sourcePath;
    }

    public String getTargetPath() {
        return targetPath;
    }
    
    public static void main (String[] args) {
    	if (args.length>0) {
    		LOG.info("mode: " +Arrays.toString(args));
    	}
        final ApplicationContext context = new ClassPathXmlApplicationContext("META-INF/spring/context/bundle-context.xml");
        KelaGenerator kelaGenerator = context.getBean(KelaGenerator.class);
        if(args.length==1 && args[0].startsWith("--options=")) {
        	kelaGenerator.setOptions(args[0].split("=")[1]);
        };
        kelaGenerator.run();
    }

    public void stop() {
    	runState = RunState.STOP_REQUESTED;
    	LOG.warn("Generation interrupt-request received.");
    	if (orgContainer!=null) {
    		orgContainer.stop();
    	}
    	if (inTurn!=null) {
    		inTurn.stop();
    	}
    }
    
    public enum RunState {
    	IDLE,
    	RUNNING,
    	STOP_REQUESTED,
    	STOPPED,
    	ERROR,
    	TRANSFER,
    	DONE
    	
    }
    public static final List<RunState> startableStates=Arrays.asList( RunState.IDLE, RunState.ERROR, RunState.STOPPED, RunState.DONE);
    
    public static final String LOGGERNAME="KelaGeneratorLogger";

    		
    private FileAppender  fa = new FileAppender(); 
    {
    	 fa.setName(LOGGERNAME);
    	 fa.setLayout(new PatternLayout("%d{ABSOLUTE} %5p - %m%n"));
    	 fa.setThreshold(Level.INFO);
    	 fa.setAppend(true);
    }
    
    @PostConstruct
    private void init() {
    	fa.setFile(optiLog.getFileName());
    	LOG.addAppender(fa);
    	fa.activateOptions();
    	
        allOptiWriters.put(optiliWriter.getFileIdentifier().toUpperCase(), optiliWriter);
        allOptiWriters.put(optiniWriter.getFileIdentifier().toUpperCase(), optiniWriter);
        allOptiWriters.put(optiolWriter.getFileIdentifier().toUpperCase(), optiolWriter);
        allOptiWriters.put(optituWriter.getFileIdentifier().toUpperCase(), optituWriter);
        allOptiWriters.put(optiyhWriter.getFileIdentifier().toUpperCase(), optiyhWriter);
        allOptiWriters.put(optiytWriter.getFileIdentifier().toUpperCase(), optiytWriter);
        allOptiWriters.put(optiorWriter.getFileIdentifier().toUpperCase(), optiorWriter);
        selectedOptiWriters.addAll(allOptiWriters.values()); //default
    }

    public void setOptions(String opts) {
    	if (!runState.equals(RunState.IDLE)) {
    		LOG.error("options may only be set when process is not running or run.");
    		return;
    	}
    	selectedOptiWriters =  new LinkedList<AbstractOPTIWriter>();
    	String[] options = opts.split(",");
    	for (String optionUnTrimmed : options) {
    		String option=optionUnTrimmed.trim();
    		if (option.equalsIgnoreCase("SENDONLY")) {
    			setSendonly(true);
    		} else if (option.equalsIgnoreCase("GENERATEONLY")) {
    			setGenerateonly(true);
    		} else if (allOptiWriters.containsKey(option.toUpperCase())) {
    			selectedOptiWriters.add(allOptiWriters.get(option.toUpperCase()));
    		} else {
    			throw new RuntimeException("Unknown option: "+option);
    		}
    	}
    	if (selectedOptiWriters.size()==0) {
    		selectedOptiWriters.addAll(allOptiWriters.values()); //default
    	}
    }

    private void releaseLogger() {
   		LOG.removeAppender(fa);
    }
    
    private RunState runState = RunState.IDLE;
	@Override
	public void run() {
		Ticker ticker  = new Ticker(this, tickerInterval);
		Thread tickerTrhead =  new Thread(ticker);
		startTime = System.currentTimeMillis();
		runState = RunState.RUNNING;
		tickerTrhead.start();
		try {
	        if (!sendonly) {
	        	this.generateKelaFiles();
	        }else{
	        	LOG.info("Skipped generate files");
	        }
	        if (!generateonly) {
	        	runState = RunState.TRANSFER;
	        	this.transferFiles();
	        }else{
	        	LOG.info("Skipped transfer files");
	        }
	        runState = RunState.DONE;
		} catch (UserStopRequestException e) {
			runState = RunState.STOPPED;
			LOG.error("Interrupted.");
		} catch (Exception ex) {
			runState = RunState.ERROR;
			LOG.error(ex.getMessage());
    		ex.printStackTrace();
    	} finally {
    		releaseLogger();
    		ticker.stop();
    		endTime=System.currentTimeMillis();
    		LOG.info("duration: "+(long)((endTime-startTime)/1000.0)+"s.");
    	}
	}
	
    public static class KelaGeneratorProgress {
    	RunState	runState;
    	public KelaGeneratorProgress(RunState runState, String phase,
				String logFileName, long time) {
			super();
			this.runState = runState;
			this.phase = phase;
			this.logFileName = logFileName;
			this.time = time;
		}
		String 		phase;
    	String 		logFileName;
    	long 		time;
    	public RunState getRunState() {
			return runState;
		}
		public String getPhase() {
			return phase;
		}
		public String getLogFileName() {
			return logFileName;
		}
		public long getTime() {
			return time;
		}
		@Override
    	public String toString() {
    		return "process:["+runState+"] phase:["+phase+"] time:["+time+"s] logfile:["+logFileName+"]";
    	}
    }
    

	private int tickerInterval = 0;
	@Value("${ticker.interval:0}")
	public void setTickerInterval(String tickerInterval) {
		this.tickerInterval = Integer.parseInt(tickerInterval);
	}
	
	public static class Ticker implements Runnable {
		KelaGenerator kg;
		boolean stop = false;
		int tickerInterval = 0;
		
		public void stop() {
			stop = true;
		}

		public Ticker(KelaGenerator kg, int tickerInterval) {
			super();
			this.kg = kg;
			this.tickerInterval = tickerInterval;
		}

		@Override
		public void run() {
			if (tickerInterval > 0) {
				LOG.info("(TICKER) interval: "+tickerInterval+"s");
				try {
					while (!stop) {
						Thread.sleep(tickerInterval * 1000);
						LOG.info("(TICKER) "+kg.getThreadState());
					}
				} catch (InterruptedException e) {
				}
			}
		}
	}
    
	public KelaGeneratorProgress getThreadState() {
		return new KelaGeneratorProgress(
				runState,
				(runState.equals(RunState.RUNNING) ? (inTurn==null ? "INIT" : inTurn.getFileIdentifier() ) : "NONE"),
				optiLog.getFileName(),
				(endTime>0 ? (long) ((endTime-startTime)/1000.0) : (long)((System.currentTimeMillis()-startTime)/1000.0)));
	}	
}
