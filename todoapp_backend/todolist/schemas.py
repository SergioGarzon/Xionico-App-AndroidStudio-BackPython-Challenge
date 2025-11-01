from ninja import Schema

class TaskSchema(Schema):
    id: int
    title: str
    description: str
    status: str

class CreateTaskSchema(Schema):
    title: str
    description: str

class StatusSchema(Schema):
    status: str

