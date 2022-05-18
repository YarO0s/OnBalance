package unittest.activity;


import com.denisov.onbalance.activity.ActivityEntity;
import com.denisov.onbalance.activity.ActivityValidationService;
import com.denisov.onbalance.auth.user.UserEntity;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ActivityValidationServiceTest {
    private ActivityValidationService activityValidationService = new ActivityValidationService();

    @DataProvider(name="colorCodes")
    public Object[][] colorCodes(){
        return new Object[][]{
                //by value
                new Object[]{"000000", true},
                new Object[]{"999999", true},
                new Object[]{"AAAAAA", true},
                new Object[]{"FFFFFF", true},

                new Object[]{"/00000", false},
                new Object[]{"00000/", false},
                new Object[]{":99999", false},
                new Object[]{"99999:", false},
                new Object[]{"@AAAAA", false},
                new Object[]{"AAAAA@", false},
                new Object[]{"GFFFFF", false},
                new Object[]{"FFFFFG", false},

                //by size
                new Object[]{"0AC9B3", true},

                new Object[]{"FFFFF", false},
                new Object[]{"FFFFFF0", false},

                //by register
                new Object[]{"FFFFFF", true},
                new Object[]{"FFFFFf", true},

                new Object[]{null, false}
        };
    }

    @Test(description = "test validates activity color code with other params being correct", dataProvider = "colorCodes")
    public void testActivityColorCodeValidation(String colorCode, boolean expectedResult){
        ActivityEntity activityEntity = new ActivityEntity("demoName", colorCode, "demoDescription",
                                             50, new UserEntity());
        boolean actualResult = activityValidationService.isValid(activityEntity);
        Assert.assertEquals(expectedResult, actualResult);
    }
}
