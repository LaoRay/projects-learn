package com.zhengtoon.framework.annualticketrecharge.common.utils;

import com.zhengtoon.framework.message.im.bean.NoticeSimpleParam;
import com.zhengtoon.framework.message.im.common.IMEntityFactory;
import com.zhengtoon.framework.message.im.service.ToonIMService;
import lombok.extern.slf4j.Slf4j;

import java.text.MessageFormat;
import java.util.UUID;

/**
 * 消息通知工具类
 *
 * @author Leiqiyun
 * @date 2018/8/10 9:24
 */
@Slf4j
public class IMUtil {

    public static final String RECHARGE_SUCCESS = "充值成功！您的订单{0}共计{1}元，已支付成功，年票有效期至{2}。祝您游览愉快。";

    public static final String RECHARGE_FAIL = "充值失败！您的订单{0}共计{1}元，支付发生异常。如已发生扣款，系统会在7个工作日之内将费用退回到您的账户；如未扣款您可重新下单充值。给您带来的不便，敬请谅解。";

    private static final String MESSAGE_TYPE = "年票充值";

    public static void notice(ToonIMService toonIMService, String userId, String template, String title, Object... objs) {
        try {
            toonIMService.sendMessage(
                    IMEntityFactory.createNoticeEntity(NoticeSimpleParam.builder()
                            .msgID(UUID.randomUUID())
                            .bizNo(UUID.randomUUID())
                            .title(title)
                            .content(MessageFormat.format(template, objs))
                            .pushInfo(MESSAGE_TYPE)
                            .toClient(userId)
                            .build()));
        } catch (Exception e) {
            log.error("消息通知发送失败，type->{}，异常：{}", title, e);
        }
    }
}
