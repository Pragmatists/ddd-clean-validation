package pl.pragmatists.infrastructure.concordion;

import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.springframework.test.context.TestContextManager;

public class RestRunner extends ConcordionRunner {

    public RestRunner(Class<?> testClass) throws InitializationError {
        super(testClass);
    }

    @Override
    protected Statement specExecStatement(Object fixture) {
     
        TestContextManager testContextManager = new TestContextManager(getTestClass().getJavaClass());
        try {
            testContextManager.prepareTestInstance(fixture);
        } catch (Exception e) {
            throw new RuntimeException("Could not create spring context!", e);
        }
        
        return super.specExecStatement(fixture);
    }
    
}