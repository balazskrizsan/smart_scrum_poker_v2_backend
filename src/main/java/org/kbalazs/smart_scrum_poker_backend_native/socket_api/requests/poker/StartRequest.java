package org.kbalazs.smart_scrum_poker_backend_native.socket_api.requests.poker;

import lombok.NonNull;

import java.util.List;

public record StartRequest(@NonNull String sprintTitle, @NonNull List<String> ticketNames)
{
}
