package com.zhengtoon.framework.ticketpurchase.common.utils;

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

    public static final String TICKET_PAY_SUCCESS = "购票成功！您的订单{0}共计{1}元，已支付成功。请于游览当日到景点凭借订单号、手机号或者身份证件及二维码领取实体票券使用，祝您旅游愉快。";

    public static final String TICKET_PAY_FAIL = "出票失败！您的订单{0}共计{1}元，支付出票发生异常。如果已发生扣款，系统会在7个工作日之内将费用退回到您的账户；如果未扣款您可重新下单购买。给您带来的不便，敬请谅解。";

    public static final String TICKET_CHANGE = "您已成功改签【{0}】的{1}张{2}，游览日期改签为{3}，祝您游览愉快。";

    public static final String TICKET_REFUND = "您购买的【{0}】{1}{2}{3}张已退票成功，退款金额为{4}元，手续费{5}元，请注意查收，欢迎您再次购票。";

    public static final String VISIT_REMIND = "您有{0}【{1}】的{2}{3}张，请带好二维码票到景点验票进入，祝您游览愉快。";

    private static final String MESSAGE_TYPE = "门票购买";

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
