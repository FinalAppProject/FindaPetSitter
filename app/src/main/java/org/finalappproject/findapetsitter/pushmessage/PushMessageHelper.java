package org.finalappproject.findapetsitter.pushmessage;

import com.parse.GetCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;

import org.finalappproject.findapetsitter.model.Request;
import org.finalappproject.findapetsitter.model.User;

import java.util.HashMap;
import java.util.Map;

/**
 * Helper class for pushing messages/notifications
 *
 * Parse server is pushing the data map to the destination FCM Token
 *
 * see: https://github.com/eddie-thp/parse-server-example/blob/master/cloud/main.js
 */
public abstract class PushMessageHelper {

    private static final String PARSE_JS_PUSH_FUNCTION = "pushRequestNotification";
    private static final String KEY_TO_USER_OBJECT_ID = "toUserObjectId";
    private static final String KEY_TO_USER_FCM_TOKEN = "toUserFcmToken";
    public static final String KEY_FROM_USER_OBJECT_ID = "fromUserObjectId";
    public static final String KEY_REQUEST_OBJECT_ID = "requestObjectId";

    public static void push(User to, final Map<String, String> data) {
        to.fetchIfNeededInBackground(new GetCallback<User>() {
            @Override
            public void done(User user, ParseException e) {
                String toUserFcmToken = user.getFcmToken();
                if (toUserFcmToken != null && !toUserFcmToken.isEmpty()) {
                    data.put(KEY_TO_USER_OBJECT_ID, user.getObjectId());
                    data.put(KEY_TO_USER_FCM_TOKEN, toUserFcmToken);

                    ParseCloud.callFunctionInBackground(PARSE_JS_PUSH_FUNCTION, data);
                } else {
                    // TODO return user feedback in case of failure
                }
            }
        });
    }

    public static void pushNotifyNewRequest(Request request) {
        Map<String, String> data = new HashMap<>();
        data.put(KEY_REQUEST_OBJECT_ID, request.getObjectId());
        push(request.getReceiver(), data);
    }

}
