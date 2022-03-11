package hu.tuku13.onlabrestapi.controller

import hu.tuku13.onlabrestapi.dto.GroupForm
import hu.tuku13.onlabrestapi.model.Group
import hu.tuku13.onlabrestapi.repository.GroupRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/groups")
class GroupController {

    @Autowired
    private lateinit var groupRepository: GroupRepository

    @GetMapping("")
    fun getGroups() : ResponseEntity<List<Group>> {
        return ResponseEntity.ok(
            groupRepository.findAll()
        )
    }

    @GetMapping("/{id}")
    fun getGroup(@PathVariable id: Long) : ResponseEntity<Group> {
        val group = groupRepository.findById(id)

        return if(group.isEmpty) {
            ResponseEntity(HttpStatus.NOT_FOUND)
        } else {
            ResponseEntity.ok(group.get())
        }

    }

    @PutMapping("/{id}")
    fun editGroup(
        @PathVariable id: Long,
        @RequestBody form : GroupForm
    ) : ResponseEntity<String> {

        if(form.groupName == null && form.description == null) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }

        val group = groupRepository.getById(id)

        if(form.groupName != null) {
            val isNameAlreadyUsed = groupRepository.existsByName(form.groupName)

            if(isNameAlreadyUsed) {
                return ResponseEntity(HttpStatus.CONFLICT)
            }

            group.apply {
                name = form.groupName
            }
        }

        if(form.description != null) {
            group.apply {
                description = form.description
            }
        }

        groupRepository.save(group)
        return ResponseEntity(HttpStatus.OK)
    }

    @PostMapping("/new")
    fun createGroup(
        @RequestBody form: GroupForm
    ) : ResponseEntity<Long> {

        if(form.groupName == null || form.description == null) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }

        val alreadyExist = groupRepository.existsByName(form.groupName)

        if (alreadyExist) {
            return ResponseEntity(HttpStatus.CONFLICT)
        }

        val group = groupRepository.save(
            Group(
                name = form.groupName,
                description = form.description,
                createdBy = form.userId
            )
        )

        return ResponseEntity(group.id, HttpStatus.CREATED)
    }



}