package org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class IdsUserInfoBatchRequest
{
    private List<String> userIds;
}
