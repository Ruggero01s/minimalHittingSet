import java.util.*;

public class Solver
{

	public List<Hypothesis> all=new ArrayList<>();

	public List<Hypothesis> solve(Instance instance)
	{
		instance.generateM1andN1();
		Hypothesis h0=new Hypothesis(Collections.nCopies(instance.getM1().size(), 0));
		setFields(instance, h0);
		List<Hypothesis> current=new ArrayList<>();
		current.add(h0);
		instance.getPerLevelHypothesis().add(1);
		List<Hypothesis> solutions=new ArrayList<>();

		while (!current.isEmpty())
		{
			long startLevelTimer=System.currentTimeMillis();
			List<Hypothesis> next=new ArrayList<>();

			for (Iterator<Hypothesis> iterator=current.iterator(); iterator.hasNext(); )
			{
				Hypothesis h=iterator.next();
				all.add(new Hypothesis(h));

				if (h.isSolution())
				{
					if (isMinimal(solutions, h))
						solutions.add(new Hypothesis(h));

					iterator.remove();
				}
				else if (h.isNullSolution())
				{
					next.addAll(generateChildren(instance, h, current));
				}
				else if (h.getBinaryRep().indexOf(1)!=0)
				{
					Hypothesis h2s=h.globalInitial();

					int preRemoveLength=current.size();
					removeAllBiggerHypothesis(current, h2s);
					int postRemoveLength=current.size();
					int removalCount=preRemoveLength-postRemoveLength;

					if (removalCount>0)
						iterator=current.listIterator(current.indexOf(h)-removalCount);

					Hypothesis hp=current.getFirst();
					if (!hp.equals(h))
						next.addAll(generateChildren(instance, h, current));
				}
				instance.updateSpatialPerformance(Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory());
			}

			long endLevelTimer=System.currentTimeMillis();
			instance.getPerLevelTime().add(((double) (endLevelTimer-startLevelTimer)/1000.0));
			instance.getPerLevelHypothesis().add(next.size());
			instance.setMaxCardExplored(instance.getMaxCardExplored()+1);
			current=next;
		}

		return solutions;
	}

	private boolean isMinimal(List<Hypothesis> solutions, Hypothesis h)
	{
		for (Hypothesis solution : solutions)
			if (solution.isSubsetOf(h))
				return false;

		return true;
	}

	private void removeAllBiggerHypothesis(List<Hypothesis> current, Hypothesis h2s)
	{
		for (int i=0; i<current.size(); i++)
			if (current.get(i).isGreater(h2s))
			{
				current.remove(i);
				i--;
			}
		//current.removeIf(hypothesis -> hypothesis.isGreater(h2s));
	}

	private List<Hypothesis> merge(List<Hypothesis> next, List<Hypothesis> hypotheses)
	{
		next.addAll(hypotheses);
		Comparator<Hypothesis> hypothesisComparator=Solver::compare;
		next.sort(hypothesisComparator);
		return next.reversed();
	}

	private static int compare(Hypothesis h1, Hypothesis h2)
	{
		if (h1.equals(h2))
			return 0;

		return h1.isGreater(h2) ? 1 : -1;
	}


	private List<Hypothesis> generateChildren(Instance instance, Hypothesis h, List<Hypothesis> current)
	{
		List<Hypothesis> children=new ArrayList<>();
		if (h.isNullSolution())
		{
			for (int i=0; i<instance.getM1().size(); i++)
			{
				Hypothesis h1=new Hypothesis(h.getBinaryRep());
				h1.getBinaryRep().set(i, 1);
				setFields(instance, h1);
				children.add(h1);
			}
			return children;
		}

		for (int i=0; i<h.getBinaryRep().indexOf(1); i++)
		{
			if (h.getBinaryRep().get(i)==0)
			{
				Hypothesis h1=new Hypothesis(h.getBinaryRep());
				h1.getBinaryRep().set(i, 1);
				setFields(instance, h1);
				h.propagate(h1);
				Hypothesis h2i=h1.initialPredecessor(h);
				Hypothesis h2f=h1.finalPredecessor(h);
				int counter=0;

				for (Hypothesis hp : current)
				{
					if (h2i.isGreaterOrEqual(hp) && hp.isGreaterOrEqual(h2f))
					{
						if (hp.hammingDist(h1)==1 && hp.hammingDist(h)==2)
						{
							hp.propagate(h1);
							counter++;
						}
					}
				}

				if (counter==h.cardinality())
					children.add(h1);
			}
		}
		return children;
	}

	private void setFields(Instance instance, Hypothesis h)
	{
		if (h.hammingDist(new Hypothesis(Collections.nCopies(instance.getM1().size(), 0)))==1)
			h.setHitVector(instance.getN1().get(h.getBinaryRep().indexOf(1)));
		else
			h.setHitVector(Collections.nCopies(instance.getN1().getFirst().size(), 0));
	}
}
