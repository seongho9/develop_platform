package me.seongho9.dev.service.development.task;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.function.Function;


@Component
public class TaskManager {

    public void registerTask(Queue<Function<String, String>> q, Function task) {
        q.add(task);
    }

    //근데, undo에서 exception이 발생하면?
    public void undo(Queue<Function<String, String>> q, Queue<String> param){
        while (!q.isEmpty()) {
            Function task = q.peek();
            task.apply(param.peek());
            param.remove();
            q.remove();
        }
    }
}
