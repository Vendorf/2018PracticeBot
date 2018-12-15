package frc.team4585.robot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class TaskSequenceGroup {
	private List<TaskSequence> _MySequenceList = new ArrayList<TaskSequence>();
	boolean _AllSequencesCompleted = false;
	String _GroupName = "";
	
	public TaskSequenceGroup(String SequenceGroupName)
	{
		_GroupName = SequenceGroupName;
	}
	
	public void AddSequence(TaskSequence NewSequence)
	{
		_MySequenceList.add(NewSequence);
	}
	
	public String GetGroupName()
	{
		return _GroupName;
	}

    /**
     * Performs tasks for autonomous based on submitted TaskSequences
     * Places TaskSequences into a threadpool, allowing them to run between autonomous ticks
     * @return true when all TaskSequence threads have terminated
     */
	public boolean DoTasks()
	{
        ExecutorService es = Executors.newFixedThreadPool(10); //arbitrary maximum of sequences
        List<Future<Boolean>> futures = null;
        try {
             futures = es.invokeAll(_MySequenceList);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for(Future<Boolean> future : futures){ //will only return true on DoTasks when all futures have returned
            if(!future.isDone()){
                _AllSequencesCompleted = false;
                break;
            }
            _AllSequencesCompleted = true;
        }
//        if(!_AllSequencesCompleted)
//		{
//			_AllSequencesCompleted = true;
//			for(TaskSequence CurSeq : _MySequenceList)
//			{
//				_AllSequencesCompleted &= CurSeq.DoCurrentTask();
//			}
//		}
		
		return _AllSequencesCompleted;
	}
	
	public boolean AllSequencesCompleted()
	{
		return _AllSequencesCompleted;
	}
}
