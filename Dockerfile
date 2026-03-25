FROM ubuntu:jammy

COPY target/smart_scrum_poker_backend_native /smart_scrum_poker_backend_native

CMD ["/smart_scrum_poker_backend_native"]
