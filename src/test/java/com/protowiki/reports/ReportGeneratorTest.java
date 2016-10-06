package com.protowiki.reports;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Nick
 */
@Ignore
public class ReportGeneratorTest {

    public Logger logger = LoggerFactory.getLogger(ReportGeneratorTest.class);

    @Rule
    public TestName testName = new TestName();

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void before() {
        logger.debug("before: {}", testName.getMethodName());
    }

    @After
    public void after() {
        logger.debug("after: {}", testName.getMethodName());
    }

    /**
     * Test of generateBasicReport method, of class ReportGenerator.
     */
    @Test
    public void testGenerateBasicReport() {

        RecordSummary rs1 = new RecordSummary();
        rs1.setRecordId("000000011");
        rs1.setFoundEnglishAbstract(true);
        rs1.setFoundHebrewAbstract(true);
        rs1.setViaf("440002211");
        rs1.setLabelEn("Brandon");
        rs1.setLabelHe("ברנדון");
        rs1.setStatus("SUCCESS");
        rs1.setDateCreated(new Date());

        RecordSummary rs2 = new RecordSummary();
        rs2.setRecordId("000000012");
        rs2.setFoundEnglishAbstract(true);
        rs2.setFoundHebrewAbstract(false);
        rs2.setViaf("440002212");
        rs2.setLabelEn("Zanthia");
        rs2.setLabelHe("זאנטיה");
        rs2.setStatus("SUCCESS");
        rs2.setDateCreated(new Date());

        RecordSummary rs3 = new RecordSummary();
        rs3.setRecordId("000000013");
        rs3.setFoundEnglishAbstract(true);
        rs3.setFoundHebrewAbstract(true);
        rs3.setViaf("440002222");
        rs3.setLabelEn("Malcolm");
        rs3.setLabelHe("מלקולם");
        rs3.setStatus("SUCCESS");
        rs3.setDateCreated(new Date());
        
        RecordSummary rs4 = new RecordSummary();
        rs4.setRecordId("012200017");
        rs4.setFoundEnglishAbstract(false);
        rs4.setFoundHebrewAbstract(false);
        rs4.setViaf("440002243");
        rs4.setLabelEn("Richard");
        rs4.setLabelHe("ריצארד");
        rs4.setStatus("FAILED");
        rs4.setDateCreated(new Date());

        List<RecordSummary> summeries = Arrays.asList(rs1, rs2, rs3, rs4);

        String fileName = "Test_Report";
        ProcessReportContext processSummery = new ProcessReportContext(summeries, "Test Report ECHO", "Some verbose description about the process");
        
        ReportGenerator reportGenerator = new ReportGenerator();
        reportGenerator.generateBasicReport(processSummery, fileName);
    }
}
