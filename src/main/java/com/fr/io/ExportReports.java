package com.fr.io;

import com.fr.base.Parameter;
import com.fr.base.operator.common.CommonOperator;
import com.fr.chart.activator.ChartBaseActivator;
import com.fr.cluster.engine.activator.standalone.StandaloneModeActivator;
import com.fr.config.activator.BaseDBActivator;
import com.fr.config.activator.ConfigurationActivator;
import com.fr.env.operator.CommonOperatorImpl;
import com.fr.general.I18nResource;
import com.fr.health.activator.ModuleHealActivator;
import com.fr.io.exporter.PageExcelExporter;
import com.fr.main.TemplateWorkBook;
import com.fr.main.workbook.PageWorkBook;
import com.fr.module.Module;
import com.fr.module.tool.ActivatorToolBox;
import com.fr.report.ReportActivator;
import com.fr.report.RestrictionActivator;
import com.fr.report.core.ReportUtils;
import com.fr.report.module.ReportBaseActivator;
import com.fr.report.report.PageReport;
import com.fr.report.write.WriteActivator;
import com.fr.scheduler.SchedulerActivator;
import com.fr.stable.PageActor;
import com.fr.store.StateServiceActivator;
import com.fr.workspace.simple.SimpleWork;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class ExportReports {
    public static void main(String[] args) {
        // 首先需要定义执行所在的环境，这样才能正确读取数据库信息
        // 定义报表运行环境,用于执行报表
        Module module = ActivatorToolBox.simpleLink(new BaseDBActivator(),
                new ConfigurationActivator(),
                new StandaloneModeActivator(),
                new ModuleHealActivator(),
                new StateServiceActivator(),
                new ChartBaseActivator(),
                new SchedulerActivator(),
                new ReportBaseActivator(),
                new RestrictionActivator(),
                new ReportActivator(),
                new WriteActivator());
        SimpleWork.supply(CommonOperator.class, new CommonOperatorImpl());
        String envpath = "//Applications//FineReport10_325//webapps//webroot//WEB-INF";//工程路径
        SimpleWork.checkIn(envpath);
        I18nResource.getInstance();
        module.start();


        // 进行程序的一些必要初始化
        try {
            // 未执行模板工作薄
            TemplateWorkBook workbook = TemplateWorkBookIO.readTemplateWorkBook(
                    "Gettingstarted.cpt");
            // 参数值为China计算结果，将结果保存至rworkbook
            Parameter[] parameters = workbook.getParameters();
            java.util.Map parameterMap = new java.util.HashMap();
            for (int i = 0; i < parameters.length; i++) {
                parameterMap.put(parameters[i].getName(), "华东");
            }
            PageWorkBook rworkbook = (PageWorkBook)workbook.execute(parameterMap,new PageActor());
            rworkbook.setReportName(0, "华东");
            // 清空parametermap，将参数值改为华北,计算后获得ResultReport
            parameterMap.clear();
            for (int i = 0; i < parameters.length; i++) {
                parameterMap.put(parameters[i].getName(), "华北");
            }
            PageWorkBook rworkbook2 = (PageWorkBook)workbook.execute(parameterMap,new PageActor());
            PageReport rreport2 = rworkbook2.getPageReport(0);
            rworkbook.addReport("华北", rreport2);
            // 将结果工作薄导出为Excel文件
            OutputStream outputStream = new FileOutputStream(new File("//Users//susie//Downloads//ExcelExport1.xls"));
            PageExcelExporter excelExport = new PageExcelExporter(ReportUtils.getPaperSettingListFromWorkBook(rworkbook));
            excelExport.export(outputStream, rworkbook);
            outputStream.close();
            module.stop();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}