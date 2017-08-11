package com.trn.spark.scraper.util

import com.markmonitor.aviator.common.utils.ConfigurationAdapter
import com.markmonitor.aviator.plugins.plugin.{PluginsToConfiguration, ScraperPluginFactory, ScraperPlugins}
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
}
