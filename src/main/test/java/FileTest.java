import com.code.Application;
import com.code.pojo.FileOption;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = Application.class)
public class FileTest {

    @Autowired
    FileOption fileOption;


    @Test
    void getPath(){
        System.out.println("input:\t"+fileOption.getInputPath());
    }
}
