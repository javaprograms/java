

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;

public class XlsWrite {
	private String[] zh_paralist = null;

	private String[] en_paralist = null;

	private int sheetindex = 0;

	private int current_row = 0;

	private jxl.write.WritableWorkbook wwb = null;

	private jxl.write.WritableSheet ws = null;

	jxl.write.DateFormat df = new jxl.write.DateFormat("yyyy-MM-dd");

	jxl.write.WritableCellFormat wcfDF = new jxl.write.WritableCellFormat(df);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		XlsWrite write = new XlsWrite("dd");

		//write.createnewRow(null, zhxx);

		/*response.setContentType("application/msexcel");
		response.setHeader("Content-Disposition", "attachment; filename="
				+ fileName);

		WritableWorkbook workbook = Workbook.createWorkbook(response
				.getOutputStream());
		WritableSheet sheet = workbook.createSheet("查询结果", 0);

		WritableCellFormat format = new WritableCellFormat();
		format.setBorder(Border.ALL, BorderLineStyle.THIN);
		format.setBackground(Colour.GRAY_25);
		WritableFont wf = new WritableFont(WritableFont.createFont("宋体"), 11,
				WritableFont.BOLD);
		format.setFont(wf);
		format.setAlignment(Alignment.CENTRE);
		format.setVerticalAlignment(VerticalAlignment.CENTRE);

		int currentRow = 0;
		Label label = new Label(0, currentRow, "序号", format);
		sheet.addCell(label);
		label = new Label(1, currentRow, "获奖人员", format);
		sheet.addCell(label);
		label = new Label(2, currentRow, "项目名称", format);
		sheet.addCell(label);
		label = new Label(3, currentRow, "项目编号", format);
		sheet.addCell(label);
		label = new Label(4, currentRow, "获奖类型", format);
		sheet.addCell(label);
		label = new Label(5, currentRow, "获奖等级", format);
		sheet.addCell(label);
		label = new Label(6, currentRow, "获奖时间", format);
		sheet.addCell(label);
		label = new Label(7, currentRow, "备注", format);
		sheet.addCell(label);

		format = new WritableCellFormat();
		format.setBorder(Border.ALL, BorderLineStyle.THIN);
		wf = new WritableFont(WritableFont.createFont("宋体"), 11,
				WritableFont.NO_BOLD);
		format.setFont(wf);
		format.setVerticalAlignment(VerticalAlignment.CENTRE);
		for (int i = 0, len = result.size(); i < len; i++) {
			Award award = result.get(i);
			currentRow++;
			String awardType = (String) dictMap.get("awardType@"
					+ award.getAwardType());
			String awardLevel = (String) dictMap.get("awardType@"
					+ award.getAwardLevel());
			Number number = new Number(0, currentRow, i + 1, format);
			sheet.addCell(number);
			label = new Label(1, currentRow, award.getAwardPeople(), format);
			sheet.addCell(label);
			label = new Label(2, currentRow, award.getProjectName(), format);
			sheet.addCell(label);
			label = new Label(3, currentRow, award.getProjectCode(), format);
			sheet.addCell(label);
			label = new Label(4, currentRow,
					awardType == null ? "" : awardType, format);
			sheet.addCell(label);
			label = new Label(5, currentRow, awardLevel == null ? ""
					: awardLevel, format);
			sheet.addCell(label);
			label = new Label(6, currentRow, DateHelper.formatDate(
					award.getAwardDate(), "yyyy"), format);
			sheet.addCell(label);
			label = new Label(7, currentRow, award.getAwardDesc() == null ? ""
					: award.getAwardDesc(), format);
			sheet.addCell(label);
		}
		sheet.setColumnView(0, 5);
		sheet.setColumnView(1, 25);
		sheet.setColumnView(2, 15);
		sheet.setColumnView(3, 15);
		sheet.setColumnView(4, 15);
		sheet.setColumnView(5, 15);
		sheet.setColumnView(6, 25);
		sheet.setColumnView(7, 25);
		workbook.write();
		workbook.close();*/

	}

	public boolean writeData(List datalist) {
		current_row=0;
		boolean rs = false;
		try {
			int size = datalist.size();
			for (int i = 0; i < size; i++) {
				current_row++;
				if (createnewRow(datalist.get(i), current_row) == false) {
					rs = false;
					break;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			rs = false;
		}
		return rs;
	}

	public XlsWrite(String fullname) {
		try {
			wwb = Workbook.createWorkbook(new File(fullname));
			//ws = wwb.createSheet("1", getSheetindex());
		} catch (IOException ex) {
			System.out.println("表格生成失败�?");
			ex.printStackTrace();
		}
	}
	public void createSheet(String name,int index){
		ws=null;
		ws = wwb.createSheet(name, index);
		 
	}

	/**
	 * 写入文件�?
	 * 
	 * @param os
	 */
	public XlsWrite(OutputStream os) {
		try {
			wwb = Workbook.createWorkbook(os);
			ws = wwb.createSheet("1", getSheetindex());
		} catch (IOException ex) {
			System.out.println("表格生成失败�?");
			ex.printStackTrace();
		}
	}

	// 关闭�?
	public boolean closeXls() {
		try {
			
			wwb.write();
			wwb.close();
			return true;
		} catch (IOException ex) {
			ex.printStackTrace();
			return false;
		} catch (jxl.write.WriteException ex) {
			ex.printStackTrace();
			return false;
		}
	}

	// 生成表格�?
	private boolean createHead(jxl.write.WritableSheet ws, String[] columninfo) {
		try {
			WritableCellFormat format = new WritableCellFormat();
			format.setBorder(Border.ALL, BorderLineStyle.THIN);
			format.setBackground(Colour.GRAY_25);
			WritableFont wf = new WritableFont(WritableFont.createFont("宋体"),
					11, WritableFont.BOLD);
			format.setFont(wf);
			format.setAlignment(Alignment.CENTRE);
			format.setVerticalAlignment(VerticalAlignment.CENTRE);
			for (int i = 0; i < columninfo.length; i++) {
				ws.addCell(new jxl.write.Label(i, 0, columninfo[i].toString(),
						format));
			}
			return true;
		} catch (jxl.write.WriteException ex) {
			System.out.println("创建表头失败�?");
			ex.printStackTrace();
			return false;
		}
	}

	//
	public boolean setParalist(String zh_names, String en_names) {
		boolean rs = false;
		zh_paralist = zh_names.split(",");
		en_paralist = en_names.split(",");
		rs = true;
		rs = zh_paralist == null ? false : createHead(ws, zh_paralist);
		return rs;
	}

	private String getUperName(String name) {
		return "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
	}

	private boolean createnewRow(Object data, int rowindex) throws Exception {
		int i = 0;
		try {
			for (i = 0; i < en_paralist.length; i++) {
				Method method = data.getClass().getMethod(
						getUperName(en_paralist[i]), null);
				String typename = method.getReturnType().getName();
				ws.addCell(getValue(method, typename, data, rowindex, i));
			}

		} catch (Exception ex) {
			System.out.println("���һ����ݳ��?" + "λ�ã�" + rowindex + "�� " + i
					+ "��");
			throw ex;
		}
		return true;

	}

	private jxl.write.WritableCell getValue(Method method, String typename,
			Object data, int rowindex, int colindex) throws Exception {
		jxl.write.WritableCell column = null;

		if (typename.equals("java.lang.String")) {
			column = new jxl.write.Label(colindex, rowindex,
					(String) method.invoke(data, null));
		} else if (typename.equals("java.lang.Double")) {
			column = new jxl.write.Number(colindex, rowindex,
					(Double) method.invoke(data, null));
		} else if (typename.equals("int")
				|| typename.equals("java.lang.Integer")) {
			column = new jxl.write.Number(colindex, rowindex,
					(java.lang.Integer) method.invoke(data, null));
		} else {

			column = new jxl.write.DateTime(colindex, rowindex,
					(Date) method.invoke(data, null), wcfDF);
		}
		return column;
	}

	public boolean writerecorde(jxl.write.WritableWorkbook wwb) {
		try {

			// "���Ҳ֧���ˣ�����Ҳ׼����ȫ�ˣ����Կ�ʼ�¹��ˣ�?"������Ҫ���ֻ��ʵ��API���ṩ��Excel��������ͣ�����?
			// ������ӵ�������оͿ����ˣ��ο�����Ĵ���Ƭ�Σ�?
			// 1.���Label����
			jxl.write.WritableFont wf = new jxl.write.WritableFont(
					WritableFont.ARIAL, 11, WritableFont.BOLD, false,
					jxl.format.UnderlineStyle.NO_UNDERLINE,
					jxl.format.Colour.BLACK);
			jxl.write.WritableCellFormat wcfF = new jxl.write.WritableCellFormat(
					wf);
			wcfF.setBackground(jxl.format.Colour.GRAY_25);
			jxl.write.Label labelC = new jxl.write.Label(0, 0,
					"This is a Label cell");
			// labelC.setCellFormat(wcfF);
			ws.addCell(labelC);
			jxl.write.Label labeld = new jxl.write.Label(0, 4, "2007-0-8");
			// labelC.setCellFormat(wcfF);
			ws.addCell(labeld);

			// ��Ӵ�������Formatting�Ķ���

			jxl.write.Label labelCF = new jxl.write.Label(1, 0,
					"This is a Label Cell", wcfF);
			ws.addCell(labelCF);
			// ��Ӵ���������ɫFormatting�Ķ���
			jxl.write.WritableFont wfc = new jxl.write.WritableFont(
					WritableFont.ARIAL, 10, WritableFont.NO_BOLD, false,
					jxl.format.UnderlineStyle.NO_UNDERLINE,
					jxl.format.Colour.RED);

			jxl.write.WritableCellFormat wcfFC = new jxl.write.WritableCellFormat(
					wfc);
			// ws.addCell(wcfFC);
			jxl.write.Label labelCFC = new jxl.write.Label(1, 0,
					"This is a Label Cell", wcfFC);
			ws.addCell(labelCFC);
			// 2.���Number����
			jxl.write.Number labelN = new jxl.write.Number(0, 1, 3.1415926);
			ws.addCell(labelN);
			// ��Ӵ���formatting��Number����
			jxl.write.NumberFormat nf = new jxl.write.NumberFormat("#.##");
			jxl.write.WritableCellFormat wcfN = new jxl.write.WritableCellFormat(
					nf);
			jxl.write.Number labelNF = new jxl.write.Number(1, 1, 3.1415926,
					wcfN);
			ws.addCell(labelNF);
			// 3.���Boolean����
			jxl.write.Boolean labelB = new jxl.write.Boolean(0, 2, false);

			ws.addCell(labelB);
			jxl.write.DateTime labelD = new jxl.write.DateTime(0, 3, new Date());
			ws.addCell(labelD);
			wwb.write();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return true;
	}

	public int getSheetindex() {
		return sheetindex;
	}

	public void setSheetindex(int sheetindex) {
		this.sheetindex = sheetindex;
	}
}
