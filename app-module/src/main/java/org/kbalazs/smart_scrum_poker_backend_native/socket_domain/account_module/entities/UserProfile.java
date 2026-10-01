package org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "User profile information")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile
{
    @JsonProperty("userId")
    @Schema(description = "User ID")
    private String userId;

    @JsonProperty("userName")
    @Schema(description = "User's full name")
    private String userName;

    @JsonProperty("userNick")
    @Schema(description = "User's nickname")
    private String userNick;
}
