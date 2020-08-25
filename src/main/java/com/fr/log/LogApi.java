package com.fr.log;

/**
 * 后台输出log信息-http://help.finereport.com/doc-view-746.html
 */
public class LogApi {
    public static void main(String[] args) {
        FineLoggerFactory.getLogger().info( "This is level info");    //only display when server setting level is info
        FineLoggerFactory.getLogger().warn("This is level warning");   // only display when server setting level is info,warning
        FineLoggerFactory.getLogger().error("This is level error");   // only display when server setting level is info,warning and error. 10.0 cancelled server level log record
    }
}
