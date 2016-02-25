package com.wikitomarc.reporting;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;

import java.math.BigDecimal;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;

import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.style.FontBuilder;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.jasperreports.engine.JRDataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Nick
 */
public class ReportFactory {

    public static Logger logger = LoggerFactory.getLogger(ReportFactory.class);

    public void Bar3DChartReport() {
        build();
    }

    private void build() {
        FontBuilder boldFont = stl.fontArialBold().setFontSize(12);
        TextColumnBuilder<String> itemColumn = col.column("Item", "item", type.stringType());
        TextColumnBuilder<Integer> quantityColumn = col.column("Quantity", "quantity", type.integerType());
        TextColumnBuilder<BigDecimal> unitPriceColumn = col.column("Unit price", "unitprice", type.bigDecimalType());

        
    }

    private JRDataSource createDataSource() {
        DRDataSource dataSource = new DRDataSource("item", "quantity", "unitprice");
        dataSource.add("Tablet", 350, new BigDecimal(300));
        dataSource.add("Laptop", 300, new BigDecimal(500));
        dataSource.add("Smartphone", 450, new BigDecimal(250));
        return dataSource;
    }
}
