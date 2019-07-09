package com.fr.data;

import com.fr.general.data.TableDataException;
import mobile.MobileCodeWSStub;

public class WebServiceWsdlTableDataDemo2 extends AbstractTableData {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String[][] data;

    public WebServiceWsdlTableDataDemo2() {
        this.data = this.getWebServiceWsdlTableData();
    }

    public int getColumnCount() throws TableDataException {
        return data[0].length;
    }

    //获取列的名称为数组中第一行的值  
    public String getColumnName(int columnIndex) throws TableDataException {
        return data[0][columnIndex];
    }

    //获取行数为数据的长度-1  
    public int getRowCount() throws TableDataException {
        return data.length - 1;
    }

    //获取值  
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data[rowIndex + 1][columnIndex];
    }

    public String[][] getWebServiceWsdlTableData() {
        try {
            String url = "http://www.webxml.com.cn/WebServices/MobileCodeWS.asmx?wsdl";
            MobileCodeWSStub stub = new MobileCodeWSStub(url);
//		      MobileCodeWSStub.GetMobileCodeInfo aa = new MobileCodeWSStub.GetMobileCodeInfo();
            MobileCodeWSStub.GetDatabaseInfo bb = new MobileCodeWSStub.GetDatabaseInfo();
//		      aa.setMobileCode("18795842832");
//		      String rs=stub.getMobileCodeInfo(aa).getGetMobileCodeInfoResult();
            String[] p = stub.getDatabaseInfo(bb).getGetDatabaseInfoResult().getString();
            String result[][] = new String[p.length][3];
            String b1, b2, b3;
            for (int i = 0; i < p.length; i++) {
                if (p[i].length() != 0) {
                    b1 = p[i].substring(0, p[i].indexOf(" "));
                    b2 = p[i].substring(p[i].indexOf(" ") + 1).substring(0, p[i].substring(p[i].indexOf(" ") + 1).indexOf(" "));
                    b3 = p[i].substring(p[i].indexOf(" ") + 1).substring(p[i].substring(p[i].indexOf(" ") + 1).indexOf(" ") + 1);
                    result[i][0] = b1;
                    result[i][1] = b2;
                    result[i][2] = b3;
                }
            }
            return result;
        } catch (java.rmi.RemoteException e) {
            e.printStackTrace();
        }
        return new String[][]{{}};
    }

    public static void main(String[] args) {
        for (int i = 0; i < new WebServiceWsdlTableDataDemo2().getWebServiceWsdlTableData().length; i++) {
            System.out.println(new WebServiceWsdlTableDataDemo2().getWebServiceWsdlTableData()[i]);
        }
    }
}