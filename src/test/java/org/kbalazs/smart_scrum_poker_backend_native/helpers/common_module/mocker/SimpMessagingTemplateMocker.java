package org.kbalazs.smart_scrum_poker_backend_native.helpers.common_module.mocker;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import static org.mockito.Mockito.mock;

public class SimpMessagingTemplateMocker
{
    private final SimpMessagingTemplate simpMessagingTemplate;

    public SimpMessagingTemplateMocker()
    {
        simpMessagingTemplate = mock(SimpMessagingTemplate.class);
    }

    public SimpMessagingTemplate getMock()
    {
        return simpMessagingTemplate;
    }
}
