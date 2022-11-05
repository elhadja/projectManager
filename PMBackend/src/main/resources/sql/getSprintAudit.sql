select _rev.id, _rev.timestamp, _rev.modified_by, 
		case
			when sprint_aud.revtype = 0 then :created_comment
            when sprint_aud.revtype = 1 and status_mod = true and status = :started then :started_comment
            when sprint_aud.revtype = 1 and status_mod = true and status = :closed then :closed_comment
		end as comment
from sprint_aud
inner join custom_revision_entity _rev
on sprint_aud.rev = _rev.id
where sprint_aud.id = :sprint_id 