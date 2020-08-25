package ltd.icecold.vexmusic.netty;


import ltd.icecold.vexmusic.interfaceservice.MessageService;

/**
 * @author ice
 */
public class MessageServiceImpl implements MessageService {
    @Override
    public String message(String msg) {
        return msg;
    }
}
