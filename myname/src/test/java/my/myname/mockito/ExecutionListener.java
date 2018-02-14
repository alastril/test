package my.myname.mockito;

import org.dbunit.IDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.dbunit.util.fileloader.XlsDataFileLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;

public class ExecutionListener implements TestExecutionListener{
	
	@Autowired
	private IDatabaseTester databaseTester;
	
	
	@Override
	public void beforeTestClass(TestContext testContext) throws Exception {
		databaseTester = testContext.getApplicationContext().getBean(IDatabaseTester.class);
//		databaseTester.setSetUpOperation(DatabaseOperation.INSERT);
	}
	@Override
	public void beforeTestMethod(TestContext testContext) throws Exception {
		DataSet dataSetAnnot = testContext.getTestMethod().getAnnotation(DataSet.class);
		if(dataSetAnnot==null) {
			return;
		}
		String pathFile = dataSetAnnot.fieldPath();
		XlsDataFileLoader xlsDataFileLoader = testContext.getApplicationContext().getBean(XlsDataFileLoader.class);
		IDataSet iDataSet = xlsDataFileLoader.load(pathFile);
		databaseTester.setDataSet(iDataSet);
		databaseTester.onSetup();
	}
	@Override
	public void afterTestMethod(TestContext testContext) throws Exception {
		
		if(databaseTester!=null) {
			databaseTester.onTearDown();
		}
	}
}
