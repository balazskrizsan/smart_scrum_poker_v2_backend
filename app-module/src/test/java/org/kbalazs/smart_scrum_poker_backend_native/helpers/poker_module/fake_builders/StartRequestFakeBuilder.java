package org.kbalazs.smart_scrum_poker_backend_native.helpers.poker_module.fake_builders;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.kbalazs.smart_scrum_poker_backend_native.socket_api.requests.poker.StartRequest;

import java.util.List;

@Accessors(fluent = true)
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StartRequestFakeBuilder
{
    String sprintTitle = "Test Sprint";
    List<String> ticketNames = List.of("Ticket 1", "Ticket 2", "Ticket 3");

    public StartRequest build()
    {
        return new StartRequest(sprintTitle, ticketNames);
    }
}
