FROM ubuntu:jammy

COPY app-module/target/smart_scrum_poker_backend_native /smart_scrum_poker_backend_native

CMD ["/smart_scrum_poker_backend_native"]
