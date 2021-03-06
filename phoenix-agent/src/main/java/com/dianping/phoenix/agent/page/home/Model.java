package com.dianping.phoenix.agent.page.home;

import com.dianping.phoenix.agent.AgentPage;
import org.unidal.web.mvc.ViewModel;

public class Model extends ViewModel<AgentPage, Action, Context> {
	public Model(Context ctx) {
		super(ctx);
	}

	@Override
	public Action getDefaultAction() {
		return Action.VIEW;
	}
}
