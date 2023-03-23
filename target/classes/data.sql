insert into service (room_service, laundry, cleaning) values (true, true, true),
                                                             (true,true, true),
                                                             (true, false, true),
                                                             (true, false, true);


insert into customer (ci,first_name, last_name, telephone,able_to_book) values (52204945, 'Ana', 'Lopez', 099105213, true),
                                                                               (45555894, 'Marcos', 'Garcia', 099123456, true),
                                                                                (84940215, 'Paula', 'Garcia', 099155513, true);

insert into room (room, room_number, has_wifi, price_per_night, has_mini_bar, has_safe_box, service) VALUES ('luxury', 10, true,5000,true,true,'ROOM_SERVICE');
insert into room (room, room_number, has_wifi, price_per_night, has_mini_bar, has_safe_box, service) VALUES ('luxury', 15, true,5000,true,true,'ROOM_SERVICE');
insert into room (room, room_number, has_wifi, price_per_night, has_mini_bar, has_safe_box, service) VALUES ('standard', 1, true,3000,false,false,'ROOM_SERVICE');
insert into room (room, room_number, has_wifi, price_per_night, has_mini_bar, has_safe_box, service) VALUES ('standard', 2, true,3000,false,false,'ROOM_SERVICE');

