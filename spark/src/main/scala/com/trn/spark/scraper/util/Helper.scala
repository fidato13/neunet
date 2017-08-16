package com.trn.spark.scraper.util

import com.markmonitor.aviator.common.utils.ConfigurationAdapter
import com.markmonitor.aviator.common.writable.LinkInterface
import com.markmonitor.aviator.plugins.plugin.parser.FetcherPlugin
import com.markmonitor.aviator.plugins.plugin.{PluginsToConfiguration, ScraperPluginFactory, ScraperPlugins}
import com.markmonitor.aviator.plugins.utils.HTTPRequestImpl.HTTPRequestFactory
import com.markmonitor.aviator.plugins.utils.{HTTPRequest, Link}
import org.apache.hadoop.conf.Configuration


object Helper {

  def getScraperPlugins: ScraperPlugins = {
    // the path to scraper plugins is "plugins/scraper/"
    // fake hadoop configurations
    val conf = ConfigurationAdapter.create(new Configuration())
    val pluginFactory = new ScraperPluginFactory(conf)

    //load all plugins from default directory
    val scraperPath = "plugins/scraper/"
    val pluginsToConfiguration = new PluginsToConfiguration(scraperPath)
    pluginsToConfiguration.save(conf)

    //load global plugins
    val globalPath = "plugins/globals/"
    val pluginsGlobals = new PluginsToConfiguration(globalPath)
    pluginsGlobals.save(conf,PluginsToConfiguration.SCRAPER_PLUGIN_GLOBALS_CONF_PREFIX, PluginsToConfiguration.SCRAPER_PLUGIN_GLOBALS_KEY)

    /**
      * This scraper Plugin is built for whole of partition ... next we will fetch plugin for every record in every partition.
      */
    pluginFactory.getPlugins
  }

  def fetchUrlResponse(record: (String, String))( implicit scraperPlugins: ScraperPlugins) = {
    //create a linkInterface object... ie link object
    val link: LinkInterface = new Link
    link.setUrl(record._2)
    link.setLinkAbstract(record._1)


    val fetcherPlugin: FetcherPlugin = scraperPlugins.getFetcherPlugin(link.getUrl)


    val httpReq: HTTPRequest = HTTPRequestFactory.create(fetcherPlugin,link)
    (link.getUrl,httpReq.fetch(link))

  }
}
