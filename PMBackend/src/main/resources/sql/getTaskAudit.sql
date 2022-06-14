(select _rev.id, _rev.timestamp, _rev.modified_by, 
		case
			when task_aud.revtype = 0 then '-- created --'
            when task_aud.revtype = 1 and user_mod = 1 and user_id is not null then 'user changed'
            when task_aud.revtype = 1 and status_mod = 1 and status = 'TODO' then 'todo'
            when task_aud.revtype = 1 and status_mod = 1 and status = 'DOING' then 'doing'
            when task_aud.revtype = 1 and status_mod = 1 and status = 'DONE' then 'done'
		end as comment
from task_aud
inner join custom_revision_entity _rev
on task_aud.rev = _rev.id
where task_aud.id = :task_id )
union
(select _rev.id, _rev.timestamp, _rev.modified_by, 
		case
			when us_task_aud.revtype = 0 then concat('added us ', user_story_id)
            when us_task_aud.revtype = 2 then concat('removed us ', user_story_id)
		end as comment
from user_story_task_aud us_task_aud
inner join custom_revision_entity _rev
on us_task_aud.rev = _rev.id
where us_task_aud.task_id = :task_id)
order by timestamp desc