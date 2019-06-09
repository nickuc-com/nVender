package nickultracraft.vender.api;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.logging.Logger;

import org.apache.commons.lang.NullArgumentException;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import nickultracraft.updater.api.UpdaterManager;

/**
 * A class UpdaterAPI.java é uma api pública usada para atualizações de plugins.
 * Carregue esta class em static na sua main, para evitar carregar ela constantemente.
 * Discord: NickUltracraft#4550
 * Mais informações: https://nickuc.tk 
*/

public class UpdaterAPI {
		
	private Plugin plugin;
	private File pluginFile;
	private boolean updateAvailable;
	private String pluginName;
	private String pluginLastVersion;
	private String pluginDownloadURL;
	private Logger logger;
	private String prefix;
	private String updaterLink;
	private String pluginCheck;
	
	private boolean DEBUG = false;
	private String API_VERSION = "1.6";
	
	public UpdaterAPI(Plugin plugin, String pluginName) {
		if(plugin == null) { new NullArgumentException("Voce nao pode deixar o valor do plugin nulo."); return; }
		this.plugin = plugin;
		this.pluginName = pluginName;
		this.logger = plugin.getLogger();
		this.prefix = "[" + pluginName.toUpperCase() + " UPDATER] ";
		this.pluginCheck = "https://www.nickuc.tk/plugin/info?" + pluginName;
		this.updaterLink = "https://www.nickuc.tk/plugin/download?id=7";
		
		setDownloadLink("https://www.nickuc.tk/plugin/download?id=3");
		
		for(File file : plugin.getDataFolder().getParentFile().listFiles()) {
			if(file.getName().contains(plugin.getName()) && (file.getName().endsWith(".jar"))) {
				this.pluginFile = file;
			}
		}
		if(pluginFile == null) this.pluginFile = new File(plugin.getDataFolder().getParentFile(), "nProtect.jar");
	}
	
	/* 
	 * Sugestão de execuções para verificar se existe um update.
	 * Você pode apenas carregar este objeto no onEnable() do seu plugin.
	 */
	public void defaultEnableExecute() {
		if(plugin == null) { new NullArgumentException("Voce nao pode deixar o valor do plugin nulo."); return; }
		new BukkitRunnable() {
			
			@Override
			public void run() {
				/* Atualiza e verifica os dados da nova versao */
				try {
					checkUpdate();
				} catch (Throwable e) {
					e.printStackTrace();
					logger.warning("Nao foi possivel verificar por novas atualizacoes.");
				}
				
				/* Baixa a nova jar da nova versao */
				try {
					downloadUpdate();
				} catch (Throwable e) {
					e.printStackTrace();
					logger.warning("Nao foi possivel realizar o download da atualizacao.");
				}
			}
		}.runTaskLaterAsynchronously(plugin, 20*30);
	}
	public void defaultDisableExecute() {
		if(plugin == null) { new NullArgumentException("Voce nao pode deixar o valor do plugin nulo."); return; }		
		/* Realiza a instalacao do arquivo baixado */
		try {
			installUpdate();
		} catch (Throwable e) {
			e.printStackTrace();
			logger.warning("Nao foi possivel realizar a instalacao da atualizacao.");
		}
	}
	
	public Plugin getPlugin() {
		return plugin;
	}
	public void setPlugin(Plugin plugin) {
		this.plugin = plugin;
	}
	public String getPluginName() {
		return pluginName;
	}
	public void setPluginName(String pluginName) {
		this.pluginName = pluginName;
	}
	public String getLastVersion() {
		return pluginLastVersion;
	}
	public void setLastVersion(String lastVersion) {
		this.pluginLastVersion = lastVersion;
	}
	public String getDownloadLink() {
		return pluginDownloadURL;
	}
	public void setDownloadLink(String downloadUrl) {
		this.pluginDownloadURL = downloadUrl;
	}
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public boolean getDebug() {
		return DEBUG;
	}
	public void setDebug(boolean debug) {
		this.DEBUG = debug;
	}
	public boolean isUpdateAvailable() {
		return updateAvailable;
	}
	public void setUpdateAvailable(boolean updateAvailable) {
		this.updateAvailable = updateAvailable;
	}
	public String getApiVersion() {
		return API_VERSION;
	}
	public File getPluginFile() {
		return pluginFile;
	}
	public void installUpdate() throws Throwable {
		if(!isUpdateAvailable()) return;
		downloadUpdater();
		logger.info(prefix + "Iniciando instalacao da atualizacao do plugin " + pluginName + " v" + plugin.getDescription().getVersion() + " -> " + getLastVersion() + "...");
		new UpdaterManager(pluginName).update(getPluginFile().getName().substring(0, getPluginFile().getName().length()-4));
	}
	public void downloadUpdate() throws Throwable {
		if(!isUpdateAvailable()) return;
		downloadUpdater();
		logger.info(prefix + "Iniciando download da atualizacao do plugin " + pluginName + " v" + plugin.getDescription().getVersion() + " -> " + getLastVersion() + "...");
		new UpdaterManager(pluginName).baixar(getDownloadLink());
	}
	public void checkUpdate() throws Throwable {
		InputStream is;
		URLConnection openConnection = new URL(pluginCheck).openConnection();
		openConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
		is = openConnection.getInputStream();
		BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
        	sb.append((char) cp);
        }
		this.pluginLastVersion = sb.toString().split("-")[0];
		this.pluginDownloadURL = sb.toString().split("-")[1];
		this.updateAvailable = !pluginLastVersion.equals(plugin.getDescription().getVersion());
	}
 	public void downloadUpdater() throws Throwable {
		if(plugin.getServer().getPluginManager().getPlugin("NickUC-Updater") == null) {
			logger.info(prefix + "Plugin de atualizacao nao foi encontrado. Realizando download automaticamente...");
			URL url = new URL(updaterLink);
	        HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
	        httpConnection.setRequestProperty("User-Agent", pluginName + "ResourceUpdater");
	        long completeFileSize = httpConnection.getContentLength();
	        if(completeFileSize == -1) {
	        	logger.warning("Ops, parece que o arquivo de download do updater esta corrompido. Download cancelado."); 
	        	return;
	        }
	        if(DEBUG) logger.info("Conexao estabelecida com sucesso. Tamanho do arquivo é de " + completeFileSize);
	        BufferedInputStream in = new BufferedInputStream(httpConnection.getInputStream());
	        if(DEBUG) logger.info("Iniciando download do arquivo jar do updater...");
	        File updaterJar = new File(plugin.getDataFolder().getParentFile(), "nUpdater.jar");
	        FileOutputStream fos = new FileOutputStream(updaterJar);
	        BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
	        byte[] data = new byte[1024];
	        int x;
	        while ((x = in.read(data, 0, 1024)) >= 0) {
	            bout.write(data, 0, x);
	        }
	        if(DEBUG) logger.info("Fechando conexoes com o site...");
	        bout.close();
			in.close();
				
			plugin.getServer().getPluginManager().loadPlugin(updaterJar);
			plugin.getServer().getPluginManager().enablePlugin(plugin.getServer().getPluginManager().getPlugin("NickUC-Updater"));
		}
	}
}