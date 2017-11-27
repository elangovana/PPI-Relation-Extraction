package ae.nlp.biocreative.awslambda;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.event.S3EventNotification.S3EventNotificationRecord;


/**
 * Hello world!
 *
 */
public class S3EventHandler implements RequestHandler<S3Event, String>
{




    public String handleRequest(S3Event input, Context context)
    {
        try
        {
            // Logger
            LambdaLogger logger = context.getLogger();

            // Get Event Record
            S3EventNotificationRecord record = input.getRecords().get(0);

            // Source File Name
            String srcFileName = record.getS3().getObject().getKey(); // Name doesn't contain any special characters

            // Destination File Name
            String distFileName = srcFileName + ".mp4";



            //LOG
            logger.log("Job Created: " );
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }

        return "OK";
    }
}