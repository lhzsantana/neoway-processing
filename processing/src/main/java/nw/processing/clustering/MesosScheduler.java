package nw.processing.clustering;

import java.util.List;

import org.apache.mesos.Protos.ExecutorID;
import org.apache.mesos.Protos.FrameworkID;
import org.apache.mesos.Protos.MasterInfo;
import org.apache.mesos.Protos.Offer;
import org.apache.mesos.Protos.OfferID;
import org.apache.mesos.Protos.SlaveID;
import org.apache.mesos.Protos.TaskStatus;
import org.apache.mesos.Scheduler;
import org.apache.mesos.SchedulerDriver;

public class MesosScheduler implements Scheduler {

	public void disconnected(SchedulerDriver arg0) {
		// TODO Auto-generated method stub
		
	}

	public void error(SchedulerDriver arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	public void executorLost(SchedulerDriver arg0, ExecutorID arg1, SlaveID arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	public void frameworkMessage(SchedulerDriver arg0, ExecutorID arg1, SlaveID arg2, byte[] arg3) {
		// TODO Auto-generated method stub
		
	}

	public void offerRescinded(SchedulerDriver arg0, OfferID arg1) {
		// TODO Auto-generated method stub
		
	}

	public void registered(SchedulerDriver arg0, FrameworkID arg1, MasterInfo arg2) {
		// TODO Auto-generated method stub
		
	}

	public void reregistered(SchedulerDriver arg0, MasterInfo arg1) {
		// TODO Auto-generated method stub
		
	}

	public void resourceOffers(SchedulerDriver arg0, List<Offer> arg1) {
		// TODO Auto-generated method stub
		
	}

	public void slaveLost(SchedulerDriver arg0, SlaveID arg1) {
		// TODO Auto-generated method stub
		
	}

	public void statusUpdate(SchedulerDriver arg0, TaskStatus arg1) {
		// TODO Auto-generated method stub
		
	}
	
}
