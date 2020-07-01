package architecture;

import top.fsky.crawler.application.concept.BusinessService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

class ApplicationLayerTest extends ArchitectureBaseTest {

    @Nested
    class use_case { 
        
        @Test
        void business_services_should_have_suffix_BusinessService() {
            classes().that().implement(BusinessService.class)
                    .should().haveSimpleNameEndingWith("BusinessService")
                    .as("It's easy for class type recognize from the suffix 'BusinessService'.")
                    .check(classes);
        }

        @Test
        void business_services_should_implemented_BusinessService() {
            classes().that().haveSimpleNameEndingWith("BusinessService")
                    .and().areNotInterfaces()
                    .should().implement(BusinessService.class)
                    .as("The business services should implement interface 'BusinessService'.")
                    .check(classes);
        }

        @Test
        void business_services_should_under_package_service() {
            classes().that().implement(BusinessService.class)
                    .should().resideInAPackage("..application.service..")
                    .as("All business service implementer file should be put in path application/service.")
                    .check(classes);
        }
    }
}
