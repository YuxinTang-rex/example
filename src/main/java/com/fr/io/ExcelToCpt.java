package com.fr.io;

import com.fr.base.operator.common.CommonOperator;
import com.fr.chart.activator.ChartBaseActivator;
import com.fr.cluster.engine.activator.standalone.StandaloneModeActivator;
import com.fr.config.activator.BaseDBActivator;
import com.fr.config.activator.ConfigurationActivator;
import com.fr.env.operator.CommonOperatorImpl;
import com.fr.general.I18nResource;
import com.fr.io.importer.Excel2007ReportImporter;
import com.fr.main.TemplateWorkBook;
import com.fr.main.impl.WorkBook;
import com.fr.module.Module;
import com.fr.module.tool.ActivatorToolBox;
import com.fr.report.ReportActivator;
import com.fr.report.RestrictionActivator;
import com.fr.report.module.ReportBaseActivator;
import com.fr.report.write.WriteActivator;
import com.fr.scheduler.SchedulerActivator;
import com.fr.store.StateServiceActivator;
import com.fr.workspace.simple.SimpleWork;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class ExcelToCpt {
    public static void main(String[] args) throws Exception {
        // first define the environment to read database info correctly
        // define the report environment to execute the report
        Module module = ActivatorToolBox.simpleLink(new BaseDBActivator(),
                new ConfigurationActivator(),
                new StandaloneModeActivator(),
                new StateServiceActivator(),
                new ChartBaseActivator(),
                new SchedulerActivator(),
                new ReportBaseActivator(),
                new RestrictionActivator(),
                new ReportActivator(),
                new WriteActivator());
        SimpleWork.supply(CommonOperator.class, new CommonOperatorImpl());
        String envpath = "D://FineReport_10.0//webapps//webroot//WEB-INF";//env path
        SimpleWork.checkIn(envpath);
        I18nResource.getInstance();
        module.start();


        File excelFile = new File("C://Users//lx//Downloads//PageSheetExcelExport.xlsx"); // get EXCEL file
        FileInputStream a = new FileInputStream(excelFile);

        TemplateWorkBook tpl = new Excel2007ReportImporter().generateWorkBookByStream(a);
        OutputStream outputStream = new FileOutputStream(new File("C://Users//lx//Downloads//abc.cpt")); // convert to cpt template
        ((WorkBook) tpl).export(outputStream);
        outputStream.close();
        module.stop();
    }
}
