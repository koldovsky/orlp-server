update account_authority
set authority_id = 2
where account_id = (select account_id from account where email = 'infolve.adm@gmail.com');