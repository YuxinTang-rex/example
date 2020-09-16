package com.fr.io;

import com.fr.base.Parameter;
import com.fr.base.operator.common.CommonOperator;
import com.fr.chart.activator.ChartBaseActivator;
import com.fr.cluster.engine.activator.standalone.StandaloneModeActivator;
import com.fr.config.activator.BaseDBActivator;
import com.fr.config.activator.ConfigurationActivator;
import com.fr.env.operator.CommonOperatorImpl;
import com.fr.general.I18nResource;
import com.fr.io.exporter.ExcelExporter;
import com.fr.io.exporter.LargeDataPageExcelExporter;
import com.fr.io.exporter.PageExcel2007Exporter;
import com.fr.io.exporter.PageExcelExporter;
import com.fr.io.exporter.PageToSheetExcel2007Exporter;
import com.fr.io.exporter.PageToSheetExcelExporter;
import com.fr.io.exporter.excel.stream.StreamExcel2007Exporter;
import com.fr.main.TemplateWorkBook;
import com.fr.main.workbook.ResultWorkBook;
import com.fr.module.Module;
import com.fr.module.tool.ActivatorToolBox;
import com.fr.report.ReportActivator;
import com.fr.report.RestrictionActivator;
import com.fr.report.core.ReportUtils;
import com.fr.report.module.ReportBaseActivator;
import com.fr.report.write.WriteActivator;
import com.fr.scheduler.SchedulerActivator;
import com.fr.stable.WriteActor;
import com.fr.store.StateServiceActivator;
import com.fr.workspace.simple.SimpleWork;

import java.io.File;
import java.io.FileOutputStream;


public class ExportExcel {
    public static void main(String[] args) {
        // first define the environment to read database info corectly
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
        String envpath = "D://FineReport_10.0//webapps//webroot//WEB-INF";//工程路径
        SimpleWork.checkIn(envpath);
        I18nResource.getInstance();
        module.start();


        ResultWorkBook rworkbook = null;
        try {
            // template workbook to be executed
            TemplateWorkBook workbook = TemplateWorkBookIO.readTemplateWorkBook("//doc-EN//Primary//Parameter//Template_Parameter.cpt");
            //get the report parameter and set the value, When exporting the embedded dataset, the dataset will be queried based on the parameter value and converted to the embedded dataset
            Parameter[] parameters = workbook.getParameters();
            parameters[0].setValue("East China");
            //define parametermap to execute the report，save the resultbook as rworkBook
            java.util.Map parameterMap = new java.util.HashMap();
            for (int i = 0; i < parameters.length; i++) {
                parameterMap.put(parameters[i].getName(), parameters[i]
                        .getValue());
            }
            // define outputstream
            FileOutputStream outputStream;

            //export excel2003
            outputStream = new FileOutputStream(new File("C://Users//lx//Downloads//ExcelExport.xls"));
            ExcelExporter excel = new ExcelExporter();
            excel.setVersion(true);
            excel.export(outputStream, workbook.execute(parameterMap,new WriteActor()));

            //export excel2007
            outputStream = new FileOutputStream(new File("C://Users//lx//Downloads//ExcelExport.xlsx"));
            StreamExcel2007Exporter excel1 = new StreamExcel2007Exporter();
            excel.export(outputStream, workbook.execute(parameterMap,new WriteActor()));

            //paginated export excel2003
            outputStream = new FileOutputStream(new File("C://Users//lx//Downloads//PageExcelExport.xls"));
            PageExcelExporter page = new PageExcelExporter(ReportUtils.getPaperSettingListFromWorkBook(workbook.execute(parameterMap,new WriteActor())));
            page.setVersion(true);
            page.export(outputStream, workbook.execute(parameterMap,new WriteActor()));

            //paginated export excel2007
            outputStream = new FileOutputStream(new File("C://Users//lx//Downloads//PageExcelExport.xlsx"));
            PageExcel2007Exporter page1 = new PageExcel2007Exporter(ReportUtils.getPaperSettingListFromWorkBook(rworkbook));
            page1.export(outputStream, workbook.execute(parameterMap,new WriteActor()));

            //PageSheet Excel Export excel2003
            outputStream = new FileOutputStream(new File("C://Users//lx//Downloads//PageSheetExcelExport.xls"));
            PageToSheetExcelExporter sheet = new PageToSheetExcelExporter(ReportUtils.getPaperSettingListFromWorkBook(workbook.execute(parameterMap,new WriteActor())));
            sheet.setVersion(true);
            sheet.export(outputStream, workbook.execute(parameterMap,new WriteActor()));

            //PageSheet Excel Export excel2007
            outputStream = new FileOutputStream(new File("C://Users//lx//Downloads//PageSheetExcelExport.xlsx"));
            PageToSheetExcel2007Exporter sheet1 = new PageToSheetExcel2007Exporter(ReportUtils.getPaperSettingListFromWorkBook(rworkbook));
            sheet1.export(outputStream, workbook.execute(parameterMap,new WriteActor()));

            //export in big data volume
            outputStream = new FileOutputStream(new File("C://Users//lx//Downloads//LargeExcelExport.zip"));
            LargeDataPageExcelExporter large = new LargeDataPageExcelExporter(ReportUtils.getPaperSettingListFromWorkBook(workbook.execute(parameterMap,new WriteActor())), true);
            large.export(outputStream, workbook.execute(parameterMap,new WriteActor()));

            outputStream.close();
            module.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}