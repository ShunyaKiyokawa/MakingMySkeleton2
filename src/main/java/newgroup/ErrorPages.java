/*
 * 404などのエラーページに推移させる機能
 */


package newgroup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;

import newgroup.repository.MyDataRepository;

public class ErrorPages {
	@Autowired
	MyDataRepository repository;
	
    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer(){
        return new ErrorMove(); //MyCustomixer();
    }

    private static class ErrorMove implements EmbeddedServletContainerCustomizer {

        @SuppressWarnings("deprecation")
		@Override
        public void customize(ConfigurableEmbeddedServletContainer factory) {
            //factory.addErrorPages(new ErrorPage(HttpStatus.BAD_REQUEST, "/400"));
            //factory.addErrorPages(new ErrorPage(HttpStatus.UNAUTHORIZED, "/401"));
            factory.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/404"));
            //factory.addErrorPages(new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/500"));
        }

    }

    @RequestMapping("/404")
    public String notFoundError() {
        return "error/404"; // templates/error/404.html
    }
}
