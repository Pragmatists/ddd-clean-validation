package pl.pragmatists.infrastructure.concordion;

import org.concordion.api.extension.Extension;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import pl.pragmatists.concordion.rest.RestExtension;
import pl.pragmatists.Application;

@RunWith(RestRunner.class)
@SpringApplicationConfiguration(classes = {Application.class})
@IntegrationTest
@WebAppConfiguration
public abstract class RestFixture {


    @Extension
    public RestExtension rest = new RestExtension()
                                        .enableCodeMirror()
                                        .includeBootstrap();

}